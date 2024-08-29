package com.amitranofinzi.vimata.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.amitranofinzi.vimata.data.dao.RelationshipDao
import com.amitranofinzi.vimata.data.dao.UserDao
import com.amitranofinzi.vimata.data.dao.WorkoutDao
import com.amitranofinzi.vimata.data.model.Relationship
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.model.Workout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Repository for managing trainer-related data, including athlete information and workouts.
 * It interacts with Firebase Firestore and the local Room database to synchronize data.
 */
class TrainerRepository(
    private val relationshipDao: RelationshipDao,
    private val userDao: UserDao,
    private val workoutDao: WorkoutDao,
    private val context: Context
) {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    /**
     * Checks if the network is available for online operations.
     *
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
     * Retrieves the list of athlete IDs associated with a specific trainer.
     *
     * @param trainerID ID of the trainer.
     * @return A list of athlete IDs.
     */
    suspend fun getAthleteIdsForCoach(trainerID: String): List<String> {
        return withContext(Dispatchers.IO) {
            if (isNetworkAvailable()) {
                try {
                    val snapshot = firestore.collection("relationships")
                        .whereEqualTo("trainerID", trainerID)
                        .get()
                        .await()


                    val athleteIds = snapshot.documents.mapNotNull { it.getString("athleteID") }

                    relationshipDao.insertAll(snapshot.documents.mapNotNull { it.toObject(Relationship::class.java) })

                    athleteIds
                } catch (e: Exception) {
                    Log.e("TrainerRepository", "Error fetching athlete IDs from Firestore", e)
                    relationshipDao.getRelationshipsByTrainerId(trainerID). mapNotNull { it.athleteID }
                }
            } else {
                relationshipDao.getRelationshipsByTrainerId(trainerID). mapNotNull { it.athleteID }
            }
        }
    }

    /**
     * Retrieves the list of athletes based on their IDs.
     *
     * @param athleteIds List of athlete IDs.
     * @return A list of athlete User objects.
     */
    suspend fun getAthletes(athleteIds: List<String>): List<User> {
        return withContext(Dispatchers.IO) {
            if (isNetworkAvailable()) {
                try {
                    val snapshot = firestore.collection("users")
                        .whereIn("uid", athleteIds)
                        .get()
                        .await()

                    val athletes = snapshot.documents.mapNotNull { document ->
                        document.toObject(User::class.java)
                    }

                    userDao.insertAll(athletes)

                    athletes
                } catch (e: Exception) {
                    Log.e("TrainerRepository", "Error fetching athletes from Firebase", e)
                    userDao.getUsersByIDs(athleteIds)
                }
            } else {
                userDao.getUsersByIDs(athleteIds)
            }
        }
    }

    /**
     * Retrieves the list of workouts assigned to a specific athlete by a specific trainer.
     *
     * @param athleteID ID of the athlete.
     * @param trainerID ID of the trainer.
     * @return A list of workouts.
     */
    suspend fun getAthleteWorkoutsByTrainer(athleteID: String, trainerID: String): List<Workout> {
        return withContext(Dispatchers.IO) {
            if (isNetworkAvailable()) {
                try {
                    val snapshot = firestore.collection("workouts")
                        .whereEqualTo("athleteID", athleteID)
                        .whereEqualTo("trainerID", trainerID)
                        .get()
                        .await()

                    val workouts = snapshot.documents.mapNotNull { document ->
                        document.toObject(Workout::class.java)
                    }

                    // Salva gli allenamenti nel database locale
                    workoutDao.insertAll(workouts)

                    workouts
                } catch (e: Exception) {
                    Log.e("TrainerRepository", "Error fetching workouts from Firebase", e)
                    // Se fallisce, prova a ottenere gli allenamenti dal database locale
                    workoutDao.getWorkoutsByAthleteAndTrainer(athleteID, trainerID)
                }
            } else {
                // Ritorna gli allenamenti da Room quando non c'è rete
                workoutDao.getWorkoutsByAthleteAndTrainer(athleteID, trainerID)
            }
        }
    }


}
