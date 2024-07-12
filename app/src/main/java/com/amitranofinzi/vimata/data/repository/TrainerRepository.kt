package com.amitranofinzi.vimata.data.repository

import android.util.Log
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.model.Workout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TrainerRepository() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getAthleteIdsForCoach(trainerID: String): List<String> {
        val snapshot = firestore.collection("relationships")
            .whereEqualTo("trainerID", trainerID)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.getString("athleteID") }
    }

    // Given a list of athlete IDs fetch athlete data
    suspend fun getAthletes(athleteIds: List<String>): List<User> {
        Log.d("TrainerRepository", "getAthletes called with athleteIds: $athleteIds")
        if (athleteIds.isEmpty()) {
            Log.d("TrainerRepository", "Empty athleteIds list")
            return emptyList()
        }
        try {
            //Query firestore database in order to find all the users with uid equal to a value in AthleteIds
            val snapshot = firestore.collection("users")
                .whereIn("uid", athleteIds)
                .get()
                .await()

            Log.d("TrainerRepository", "Fetched documents: ${snapshot.documents.map { it.id }}")

            val athletes = snapshot.documents.mapNotNull { document ->
                try {
                    Log.d("TrainerRepository", "Document data: ${document.data}")
                    val athlete = document.toObject(User::class.java)
                    if (athlete != null) {
                        Log.d("TrainerRepository", "User found: $athlete")
                    } else {
                        Log.d("TrainerRepository", "Document ${document.id} could not be converted to User")
                    }
                    athlete
                } catch (e: Exception) {
                    Log.e("TrainerRepository", "Error converting document to User: ${document.id}", e)
                    null
                }
            }

            Log.d("TrainerRepository", "Converted users: $athletes")
            return athletes

        } catch (e: Exception) {
            Log.e("TrainerRepository", "Error fetching users", e)
            return emptyList()
        }
    }

    suspend fun getAthleteWorkoutsByTrainer(athleteID: String, trainerID: String): List<Workout> {
        return try {
            val snapshot = firestore.collection("workouts")
                .whereEqualTo("athleteID", athleteID)
                .whereEqualTo("trainerID", trainerID)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.toObject(Workout::class.java)
            }
        } catch (e: Exception) {
            // Log the error and return an empty list or handle the error as needed
            Log.e("FirestoreQuery", "Error fetching workouts", e)
            emptyList()
        }
    }


}
