package com.amitranofinzi.vimata.data.repository

import android.util.Log
import com.amitranofinzi.vimata.data.model.User
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
        if (athleteIds.isEmpty()) return emptyList()
        val snapshot = firestore.collection("users")
            .whereIn("uid", athleteIds)
            .get()
            .await()
        return snapshot.documents.map { it.toObject(User::class.java)!! }
    }


}
