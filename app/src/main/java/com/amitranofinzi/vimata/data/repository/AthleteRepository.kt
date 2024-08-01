package com.amitranofinzi.vimata.data.repository

import com.amitranofinzi.vimata.data.dao.RelationshipDao
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.amitranofinzi.vimata.data.dao.ChatDao
import com.amitranofinzi.vimata.data.dao.UserDao
import com.amitranofinzi.vimata.data.dao.WorkoutDao
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Relationship
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.model.Workout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.net.HttpURLConnection
import java.net.URL

class AthleteRepository(
    private val relationshipDao: RelationshipDao,
    private val userDao: UserDao,
    private val workoutDao: WorkoutDao,
    private val chatDao: ChatDao,
    private val context: Context // Added context to check network availability
) {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

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

    // Fetch trainer IDs for a given athleteID
    suspend fun getTrainerIdsForAthlete(athleteID: String): List<String> {
        return if (isNetworkAvailable()) {
            try {
                val snapshot = firestore.collection("relationships")
                    .whereEqualTo("athleteID", athleteID)
                    .get()
                    .await()
                val remoteTrainerIds = snapshot.documents.mapNotNull { it.getString("trainerID") }

                // Update local DB
                val relationships = snapshot.documents.mapNotNull { it.toObject(Relationship::class.java) }
                relationshipDao.insertAll(relationships)

                remoteTrainerIds
            } catch (e: Exception) {
                Log.e("AthleteRepository", "Error fetching trainer IDs from Firebase", e)
                relationshipDao.getWhereEqual("athleteID", athleteID).mapNotNull { it.trainerID }
            }
        } else {
            relationshipDao.getWhereEqual("athleteID", athleteID).mapNotNull { it.trainerID }
        }
    }

    // Get trainers as a list of User
    suspend fun getTrainers(trainerIds: List<String>): List<User> {
        return if (isNetworkAvailable()) {
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
                userDao.getWhereIn("uid", trainerIds)
            }
        } else {
            userDao.getWhereIn("uid", trainerIds)
        }
    }

    // Fetch workouts for a given athleteID
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
                workoutDao.insertAll(workouts)
                workouts
            } catch (e: Exception) {
                Log.e("AthleteRepository", "Error fetching workouts from Firebase", e)
                workoutDao.getWhereEqual("athleteID", athleteID)
            }
        } else {
            workoutDao.getWhereEqual("athleteID", athleteID)
        }
    }

    // Fetch workout PDF
    suspend fun getWorkoutPdf(workoutId: String): ByteArray? {
        val workout = workoutDao.getWithPrimaryKey(workoutId)
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

    // Get user by email
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
                    userDao.insert(user)
                }

                user
            } catch (e: Exception) {
                Log.e("AthleteRepository", "Error getting user by email: $email", e)
                userDao.getWhereEqual("email", email).firstOrNull()
            }
        } else {
            userDao.getWhereEqual("email", email).firstOrNull()
        }
    }

    // Add trainer relationship
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
                relationshipDao.insert(relationship.copy(id = generatedId))
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

    // Start new chat
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
            chatDao.insert(newChat.copy(chatId = generatedId))
            Log.d("AthleteRepository", "New chat started successfully")
        } catch (e: Exception) {
            Log.e("AthleteRepository", "Error starting new chat", e)
        }
    }
}
