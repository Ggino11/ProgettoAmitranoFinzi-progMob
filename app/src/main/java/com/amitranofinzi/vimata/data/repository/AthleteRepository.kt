package com.amitranofinzi.vimata.data.repository

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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/**
 * Repository for managing athlete-related data, including access to data from Firestore and a local database.
 */
class AthleteRepository(
    private val relationshipDao: RelationshipDao,
    private val userDao: UserDao,
    private val workoutDao: WorkoutDao,
    private val chatDao: ChatDao,
    private val context: Context // Added context to check network availability
) {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    /**
     * Checks if the network is available on the device.
     * @return True if the network is available, false otherwise.
     */
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

    /**
     * Fetches trainer IDs for a given athlete ID from Firestore or local database if the network is unavailable.
     * @param athleteID The ID of the athlete.
     * @return A list of trainer IDs associated with the athlete.
     */
    suspend fun getTrainerIdsForAthlete(athleteID: String): List<String> {
        return   withContext(Dispatchers.IO) {
            if (isNetworkAvailable()) {
            try {
                Log.d("AthleteRepository", "Network available, fetching from Firestore")
                val snapshot = firestore.collection("relationships")
                    .whereEqualTo("athleteID", athleteID)
                    .get()
                    .await()
                val remoteTrainerIds = snapshot.documents.mapNotNull { it.getString("trainerID") }

                // Aggiorna il DB locale
                val relationships = snapshot.documents.mapNotNull { document ->
                    document.toObject(Relationship::class.java)?.apply {
                        id = document.id
                    }
                }

                Log.d("AthleteRepository", "Fetched ${relationships.size} relationships from Firestore")

                try {
                    Log.d("AthleteRepository", "Inserting ${relationships.size} relationships into local DB")
                    relationshipDao.insertAll(relationships)
                    Log.d("AthleteRepository", "Insert completed successfully")
                } catch (e: Exception) {
                    Log.e("AthleteRepository", "Error inserting relationships into local DB", e)
                }


                remoteTrainerIds
            } catch (e: Exception) {
                Log.e("AthleteRepository", "Error fetching trainer IDs from Firebase", e)
                    Log.d("AthleteRepository", "Fetching trainer IDs from local DB due to error")
                    relationshipDao.getWhereEqual("athleteID", athleteID)
                        .mapNotNull { it.trainerID }
            }
        } else {
            Log.d("AthleteRepository", "No network, fetching trainer IDs from local DB")
            val localTrainerIds = relationshipDao.getWhereAthleteID(athleteID)
                .mapNotNull { it.trainerID }
            Log.d("AthleteRepository", "Fetched ${localTrainerIds.size} trainer IDs from local DB")
            localTrainerIds

        }
    }
}




    /**
     * Retrieves trainers as a list of User objects based on the provided trainer IDs.
     * Fetches from Firestore if the network is available, otherwise fetches from the local database.
     * @param trainerIds The list of trainer IDs.
     * @return A list of User objects representing trainers.
     */
    suspend fun getTrainers(trainerIds: List<String>): List<User> {
        return  withContext(Dispatchers.IO) {
            if (isNetworkAvailable()) {
                try {
                    val snapshot = firestore.collection("users")
                        .whereIn("uid", trainerIds)
                        .get()
                        .await()
                    val trainers = snapshot.documents.mapNotNull { document ->
                        document.toObject(User::class.java)
                    }

                    // Update local DB
                    userDao.insertAll(trainers)

                    trainers
                } catch (e: Exception) {
                    Log.e("AthleteRepository", "Error fetching trainers from Firebase", e)
                    userDao.getUsersByIDs( trainerIds)

                }
            } else {
                userDao.getUsersByIDs( trainerIds)
            }

        }
    }


    /**
     * Fetches workouts for a given athlete ID from Firestore or the local database if the network is unavailable.
     * @param athleteID The ID of the athlete.
     * @return A list of Workout objects associated with the athlete.
     */
    suspend fun getAthletesWorkouts(athleteID: String): List<Workout> {
        return if (isNetworkAvailable()) {
            try {
                val snapshot = firestore.collection("workouts")
                    .whereEqualTo("athleteID", athleteID)
                    .get()
                    .await()
                val workouts = snapshot.documents.mapNotNull { document ->
                    document.toObject(Workout::class.java)
                }

                // Update local DB
                withContext(Dispatchers.IO) {
                    workoutDao.insertAll(workouts)
                }
                workouts
            } catch (e: Exception) {
                Log.e("AthleteRepository", "Error fetching workouts from Firebase", e)
                withContext(Dispatchers.IO) {
                    workoutDao.getWorkoutsByAthlete(athleteID)
                }
            }
        } else {
            withContext(Dispatchers.IO) {
                workoutDao.getWorkoutsByAthlete(athleteID)
            }
        }
    }

    /**
     * Fetches a workout PDF file based on the workout ID.
     * @param workoutId The ID of the workout.
     * @return A ByteArray containing the PDF data, or null if the file cannot be fetched.
     */
    suspend fun getWorkoutPdf(workoutId: String): ByteArray? {
        val workout = workoutDao.getWorkoutById(workoutId)
        val pdfUrl = workout?.pdfUrl

        return if (pdfUrl != null && isNetworkAvailable()) {
            try {
                val url = URL(pdfUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    inputStream.readBytes()
                } else {
                    Log.e("AthleteRepository", "Failed to load PDF. Response code: ${connection.responseCode}")
                    null
                }
            } catch (e: Exception) {
                Log.e("AthleteRepository", "Error loading PDF", e)
                null
            }
        } else {
            null
        }
    }

    /**
     * Fetches a user by their email address from Firestore or the local database if the network is unavailable.
     * @param email The email address of the user.
     * @return A User object if found, or null otherwise.
     */
    suspend fun getUserByEmail(email: String): User? {
        return if (isNetworkAvailable()) {
            try {
                val snapshot = firestore.collection("users")
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .await()
                val user = snapshot.documents
                    .firstOrNull()
                    ?.toObject(User::class.java)

                // Update local DB
                if (user != null) {
                    withContext(Dispatchers.IO) {
                        userDao.insert(user)
                    }
                }

                user
            } catch (e: Exception) {
                Log.e("AthleteRepository", "Error getting user by email: $email", e)
                withContext(Dispatchers.IO) {
                    userDao.getUserByEmail( email)
                }
            }
        } else {
            withContext(Dispatchers.IO) {
                userDao.getUserByEmail( email)
            }
        }
    }

    /**
     * Adds a trainer relationship for a given athlete and trainer ID to Firestore and the local database.
     * @param trainerId The ID of the trainer.
     * @param currentUserId The ID of the current user (athlete).
     * @return The generated relationship ID, or null if the relationship could not be added.
     */
    suspend fun addTrainerRelationship(trainerId: String?, currentUserId: String): String? {
        return if (isNetworkAvailable()) {
            Log.d("AddTrainer", "$trainerId $currentUserId")
            try {
                val relationship = Relationship(
                    id = "", // generated by firestore
                    athleteID = currentUserId,
                    trainerID = trainerId
                )
                Log.d("AthleteReference", relationship.toString())
                val relationshipReference = firestore.collection("relationships")
                    .add(relationship)
                    .await()

                Log.d("AthleteReference", relationshipReference.toString())

                // generated id
                val generatedId = relationshipReference.id

                Log.d("AthleteReference", generatedId)
                val document = firestore.collection("relationships").document(generatedId)
                Log.d("AthleteDocument", document.toString())
                document.update("id", generatedId).await()
                Log.d("AthleteDocument", relationship.toString())

                // Update local DB
                withContext(Dispatchers.IO) {
                    relationshipDao.insert(relationship.copy(id = generatedId))
                }
                generatedId
            } catch (e: Exception) {
                Log.e("AthleteRepository", "Error adding relationship", e)
                null
            }
        } else {
            Log.e("AthleteRepository", "No network available to add relationship")
            null
        }
    }

    /**
     * Starts a new chat between the current user (athlete) and a trainer.
     * @param email The email address of the trainer.
     * @param currentUserId The ID of the current user (athlete).
     */
    suspend fun startNewChat(email: String, currentUserId: String) {
        if (!isNetworkAvailable()) {
            Log.e("AthleteRepository", "No network available to start new chat")
            return
        }

        try {
            // Find the trainer by email to get trainerId
            val trainer = getUserByEmail(email)
            if (trainer == null) {
                Log.e("AthleteRepository", "Trainer with email $email not found")
                return
            }
            // Add trainer relationship
            val relationshipId = addTrainerRelationship(trainer.uid, currentUserId)

            // Create a new Chat object
            if (relationshipId == null) {
                Log.e("AthleteRepository", "Relationship between athlete and trainer not found")
                return
            }
            val newChat = Chat(
                chatId = "",
                relationshipID = relationshipId,
                lastMessage = "New Chat Started"
            )
            // Add the Chat object to Firestore
            val chatReference = firestore.collection("chats")
                .add(newChat)
                .await()

            // Update id generated
            val generatedId = chatReference.id
            val document = firestore.collection("chats").document(generatedId)
            Log.d("CreationChat", document.toString())
            document.update("chatId", generatedId).await()

            // Update local DB
            withContext(Dispatchers.IO) {
                chatDao.insert(newChat.copy(chatId = generatedId))
            }
            Log.d("AthleteRepository", "New chat started successfully")
        } catch (e: Exception) {
            Log.e("AthleteRepository", "Error starting new chat", e)
        }
    }
}
