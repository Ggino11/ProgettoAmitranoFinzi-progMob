package com.amitranofinzi.vimata.data.repository

import android.util.Log
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.model.Workout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.net.HttpURLConnection
import java.net.URL

class AthleteRepository(
) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    suspend fun getAthletesWorkouts() : List<Workout> {
        val snapshot = firestore.collection("workouts")
            .get()
            .await()
        return snapshot.documents.mapNotNull { document->
            document.toObject(Workout::class.java)
        }
    }

    suspend fun getWorkoutPdf(workoutId: String): ByteArray? {
        val document = firestore.collection("workouts")
            .document(workoutId)
            .get()
            .await()

        return document.get("pdfUrl")?.let { pdfUrl ->

            try {
                val url = URL(pdfUrl as String)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    return@let inputStream.readBytes()
                } else {
                    Log.e("AthleteRepository", "Failed to load PDF. Response code: ${connection.responseCode}")
                    null
                }
            } catch (e: Exception) {
                Log.e("AthleteRepository", "Error loading PDF", e)
                null
            }
        }
    }
}
