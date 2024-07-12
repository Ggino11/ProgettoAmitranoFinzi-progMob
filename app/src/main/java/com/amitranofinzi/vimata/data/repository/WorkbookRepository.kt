package com.amitranofinzi.vimata.data.repository

import android.util.Log
import com.amitranofinzi.vimata.data.model.Collection
import com.amitranofinzi.vimata.data.model.Exercise
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class WorkbookRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getCollections(trainerID: String): List<Collection> {
        val snapshot = firestore.collection("collections")
            .whereEqualTo("trainerID", trainerID)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject(Collection::class.java) }
    }

    suspend fun getExercises(collectionID: String?): List<Exercise> {
        val snapshot = firestore.collection("workbook")
            .whereEqualTo("collectionID", collectionID)
            .get()
            .await()
        return snapshot.documents.mapNotNull { it.toObject(Exercise::class.java) }
    }

    suspend fun addCollection(collection: Collection) {
        firestore.collection("collections").add(collection).await()
    }
    suspend fun getCollectionByID(collectionID: String): Collection {

        val document = firestore.collection("collections")
            .document(collectionID)
            .get()
            .await()
       return document.toObject(Collection::class.java)!!
    }
    suspend fun uploadExercise(exercise: Exercise) {
        val firestore = FirebaseFirestore.getInstance()

        try {
            val result = firestore.collection("workbook")
                .add(exercise)
                .await()

            // Assegnamento dell'ID generato da Firebase all'oggetto Exercise
            val exerciseWithId = exercise.copy(id = result.id)

            firestore.collection("workbook")
                .document(result.id)
                .set(exerciseWithId)

            Log.d("WorkbookRepository", "Exercise uploaded successfully with ID: ${result.id}")
        } catch (e: Exception) {
            Log.e("WorkbookRepository", "Error uploading exercise", e)
        }
    }

    suspend fun getExercisesByTrainerId(trainerID: String): List<Exercise> {
        return try {
            Log.d("WorkbookRepository", "Fetching exercises for trainerID: $trainerID")
            val snapshot = firestore.collection("workbook")
                .whereEqualTo("trainerID", trainerID)
                .get()
                .await()

            val exercises = snapshot.documents.mapNotNull { it.toObject(Exercise::class.java) }
            Log.d("WorkbookRepository", "Fetched ${exercises.size} exercises")
            exercises
        } catch (e: Exception) {
            Log.e("WorkbookRepository", "Error fetching exercises", e)
            emptyList()
        }
    }


}
