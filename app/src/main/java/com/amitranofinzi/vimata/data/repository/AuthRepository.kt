package com.amitranofinzi.vimata.data.repository

//import com.google.firebase.firestore.FirebaseFirestore
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.amitranofinzi.vimata.data.dao.ChatDao
import com.amitranofinzi.vimata.data.dao.RelationshipDao
import com.amitranofinzi.vimata.data.dao.UserDao
import com.amitranofinzi.vimata.data.dao.WorkoutDao
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Relationship
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
    private val context: Context
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
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val result = syncUserData()
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun syncUserData(): Result<Unit> {
        return try {
            if (isNetworkAvailable()) {
                val currentUser = firebaseAuth.currentUser ?: return Result.failure(Exception("No current user"))

                val snapshot = firestore.collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()

                val remoteUser = snapshot.toObject(User::class.java)
                if (remoteUser != null) {

                    val usersSnapshot = firestore.collection("users").get().await()
                    val users = usersSnapshot.documents.mapNotNull { it.toObject(User::class.java) }

                    val relationshipSnapshot = firestore.collection("relationships").get().await()
                    val relationships = relationshipSnapshot.documents.mapNotNull { it.toObject(Relationship::class.java) }

                    val chatSnapshot = firestore.collection("chats").get().await()
                    val chats = chatSnapshot.documents.mapNotNull { it.toObject(Chat::class.java) }

                    val workoutsSnapshot = firestore.collection("workouts").get().await()
                    val workouts = workoutsSnapshot.documents.mapNotNull { it.toObject(Workout::class.java) }

                    if(remoteUser.userType == "trainer"){
                        //collections
                        //workbook
                    }

                    withContext(Dispatchers.IO) {
                        userDao.insertAll(users)
                    }
                    withContext(Dispatchers.IO) {
                        relationshipDao.insertAll(relationships)
                    }
                    withContext(Dispatchers.IO) {
                        chatDao.insertAll(chats)
                    }
                    withContext(Dispatchers.IO) {
                        workoutDao.insertAll(workouts)
                    }
                        /*
                        messageDao.insertAll(messages)
                        testSetDao.insertAll(testSets)
                        testDao.insertAll(tests)
                        */
                        

                    Result.success(Unit)
                } else {
                    Result.failure(Exception("User not found in Firebase"))
                }
            } else {
                Result.failure(Exception("No network available"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error during data synchronization", e)
            Result.failure(e)
        }
    }

    // Funzione per controllare la disponibilità di rete

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
