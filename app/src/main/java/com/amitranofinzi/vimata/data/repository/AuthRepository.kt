package com.amitranofinzi.vimata.data.repository

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.amitranofinzi.vimata.data.dao.ChatDao
import com.amitranofinzi.vimata.data.dao.CollectionDao
import com.amitranofinzi.vimata.data.dao.ExerciseDao
import com.amitranofinzi.vimata.data.dao.MessageDao
import com.amitranofinzi.vimata.data.dao.RelationshipDao
import com.amitranofinzi.vimata.data.dao.TestDao
import com.amitranofinzi.vimata.data.dao.TestSetDao
import com.amitranofinzi.vimata.data.dao.UserDao
import com.amitranofinzi.vimata.data.dao.WorkoutDao
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Collection
import com.amitranofinzi.vimata.data.model.Exercise
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.data.model.Relationship
import com.amitranofinzi.vimata.data.model.Test
import com.amitranofinzi.vimata.data.model.TestSet
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.model.Workout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepository(
    private val relationshipDao: RelationshipDao,
    private val userDao: UserDao,
    private val workoutDao: WorkoutDao,
    private val chatDao: ChatDao,
    private val context: Context,
    private val exerciseDao: ExerciseDao,
    private val collectionDao: CollectionDao,
    private val testSetDao: TestSetDao,
    private val testDao: TestDao,
    private val messageDao: MessageDao
) {


    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    //function to check if email already exists
    suspend fun checkEmailExists(email: String): Boolean {
        //check if email is already registered in firestore
        val usersCollection = firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()
        return !usersCollection.isEmpty //if is not empty return true

  }
    suspend fun register(email: String, password: String, userType: String, name: String, surname: String): Result<Unit> {
        return try {
            val authResult = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user
            val userId = user?.uid ?: throw IllegalStateException("User ID is null")

            val newUser = User(
                uid = userId,
                name = name,
                surname = surname,
                email = email,
                userType = userType,
                password = password
            )

            firestore.collection("users").document(userId).set(newUser).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Log the beginning of the login process
                Log.d("LoginProcess", "Starting login for email: $email")

                // Perform the login operation
                firebaseAuth.signInWithEmailAndPassword(email, password).await()

                // Log successful login
                Log.d("LoginProcess", "Login successful for email: $email")

                // Start data synchronization after successful login
                val syncResult = syncUserData()

                if (syncResult.isSuccess) {
                    // Log successful synchronization
                    Log.d("LoginProcess", "Data synchronization successful for email: $email")
                    // Return success result
                    Result.success(Unit)
                } else {
                    // Log synchronization failure
                    Log.e("LoginProcess", "Data synchronization failed for email: $email", syncResult.exceptionOrNull())
                    // Return failure result with the encountered exception
                    Result.failure(syncResult.exceptionOrNull() ?: Exception("Unknown sync error"))
                }
            } catch (e: Exception) {
                // Log the exception encountered during login or synchronization
                Log.e("LoginProcess", "Login or synchronization failed for email: $email", e)
                // Return failure result with the encountered exception
                Result.failure(e)
            }
        }
    }


    private suspend fun syncUserData(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                if (isNetworkAvailable()) {
                    val currentUser = firebaseAuth.currentUser
                        ?: return@withContext Result.failure(Exception("No current user"))

                    Log.d("SyncUserData", "Current user UID: ${currentUser.uid}")

                    val snapshot = firestore.collection("users")
                        .document(currentUser.uid)
                        .get()
                        .await()

                    val remoteUser = snapshot.toObject(User::class.java)
                    if (remoteUser != null) {
                        Log.d("SyncUserData", "Remote user found: ${remoteUser.uid}")

                        // Fetch data from Firebase
                        val usersSnapshot = firestore.collection("users").get().await()
                        val users = usersSnapshot.documents.mapNotNull {
                            Log.d("SyncUserData", "User document: ${it.id}")
                            it.toObject(User::class.java)
                        }

                        val relationshipSnapshot = firestore.collection("relationships").get().await()
                        val relationships = relationshipSnapshot.documents.mapNotNull {
                            Log.d("SyncUserData", "Relationship document: ${it.id}")
                            it.toObject(Relationship::class.java)
                        }

                        val testSetSnapshot = firestore.collection("testSets").get().await()
                        val testSets = testSetSnapshot.documents.mapNotNull {
                            Log.d("SyncUserData", "TestSet document: ${it.id}")
                            it.toObject(TestSet::class.java)
                        }

                        val chatSnapshot = firestore.collection("chats").get().await()
                        val chats = chatSnapshot.documents.mapNotNull {
                            Log.d("SyncUserData", "Chat document: ${it.id}")
                            it.toObject(Chat::class.java)
                        }

                        val workoutsSnapshot = firestore.collection("workouts").get().await()
                        val workouts = workoutsSnapshot.documents.mapNotNull {
                            Log.d("SyncUserData", "Workout document: ${it.id}")
                            it.toObject(Workout::class.java)?.also { workout ->
                                if (workout.trainerID.isNullOrEmpty() || workout.athleteID.isNullOrEmpty()) {
                                    Log.e("SyncUserData", "Invalid Workout: $workout")
                                } else {
                                    Log.d("SyncUserData", "Valid Workout: $workout")
                                }
                            }
                        }
                        val exercisesSnapshot = firestore.collection("exercises").get().await()
                        val exercises = exercisesSnapshot.documents.mapNotNull {
                            Log.d("SyncUserData", "Exercise document: ${it.id}")
                            it.toObject(Exercise::class.java)
                        }

                        val collectionsSnapshot = firestore.collection("collections").get().await()
                        val collections = collectionsSnapshot.documents.mapNotNull {
                            Log.d("SyncUserData", "Collection document: ${it.id}")
                            it.toObject(Collection::class.java)
                        }

                        val testsSnapshot = firestore.collection("tests").get().await()
                        val tests = testsSnapshot.documents.mapNotNull {
                            Log.d("SyncUserData", "Test document: ${it.id}")
                            it.toObject(Test::class.java)
                        }

                        val messagesSnapshot = firestore.collection("messages").get().await()
                        val messages = messagesSnapshot.documents.mapNotNull {
                            it.toObject(Message::class.java)
                        }

                        // Insert data into Room database
                        try {

                            Log.d("SyncUserData", "Inserting data into Room database")
                            userDao.insertAll(users)
                            Log.d("authRepo", relationships.toString())
                            relationshipDao.insertAll(relationships)
                            Log.d("authRepo", testSets.toString())

                            testSetDao.insertAll(testSets)
                            Log.d("authRepo", collections.toString())
                            collectionDao.insertAll(collections)
                            Log.d("SyncUserData", "Collections inserted into database: ${collectionDao.getAll().toString()}")
                            workoutDao.insertAll(workouts)
                            exerciseDao.insertAll(exercises)
                            chatDao.insertAll(chats)
                            testDao.insertAll(tests)
                            messageDao.insertAll(messages)
                            Log.d("SyncUserData", "Data insertion successful")

                        } catch (e: SQLiteConstraintException) {
                            Log.e("SyncUserData", "Foreign key constraint failed during insert", e)
                            return@withContext Result.failure(e)
                        }

                        Result.success(Unit)
                    } else {
                        Log.e("SyncUserData", "User not found in Firebase")
                        Result.failure(Exception("User not found in Firebase"))
                    }
                } else {
                    Log.e("SyncUserData", "No network available")
                    Result.failure(Exception("No network available"))
                }
            } catch (e: Exception) {
                Log.e("SyncUserData", "Error during data synchronization", e)
                Result.failure(e)
            }
        }
    }



    fun signOut() {
        firebaseAuth.signOut()
    }


    // --------------------- GET FUNCTIONS-------------------------//
    suspend fun getUser(userId: String): User? {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error fetching user", e)
            null
        }
    }

    suspend fun getUserType(userId: String): Result<String> {
        return try {
            val document = firestore.collection("users").document(userId).get().await()
            val userType = document.getString("userType")
            if (userType != null) {
                Result.success(userType)
            } else {
                Result.failure(Exception("UserType not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
