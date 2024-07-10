package com.amitranofinzi.vimata.data.repository

import android.util.Log
import com.amitranofinzi.vimata.data.model.Exercise
import com.amitranofinzi.vimata.data.model.Collection
import com.google.firebase.firestore.FieldValue
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
    suspend fun addExerciseToCollection(collectionId: String, exercise: Exercise) {
        val collectionRef = firestore.collection("collections").document(collectionId)
        collectionRef.update("exercises", FieldValue.arrayUnion(exercise)).await()
    }


}
