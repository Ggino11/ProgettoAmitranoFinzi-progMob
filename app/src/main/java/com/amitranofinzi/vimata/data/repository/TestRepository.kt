package com.amitranofinzi.vimata.data.repository

import android.util.Log
import com.amitranofinzi.vimata.data.model.Test
import com.amitranofinzi.vimata.data.model.TestSet
import com.amitranofinzi.vimata.data.model.Workout
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TestRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getTestSetsForAthlete(athleteID: String): List<TestSet> {
        Log.d("TestRepository", "getTestSetsForAthlete called with athleteIds: $athleteID")

        try {
            val snapshot = firestore.collection("testSets")
                .whereEqualTo("athleteID", athleteID)
                .get()
                .await()

            Log.d("testRepository", "Fetched documents: ${snapshot.documents.map { it.id }}")

            val testSets = snapshot.documents.mapNotNull { document ->
                try {
                    val testSet = document.toObject(TestSet::class.java)
                    if (testSet != null) {
                        Log.d("TestRepository", "TestSet found: $testSet")
                    } else {
                        Log.d(
                            "TestRepository",
                            "Document ${document.id} could not be converted to TestSet"
                        )
                    }
                    testSet
                } catch (e: Exception) {
                    Log.e(
                        "testRepository",
                        "error converting document to TestSet  = ${athleteID}",
                        e
                    )
                    null
                }
            }
            return testSets
        }catch (e: Exception){
            Log.e("TestRepository", "Error fetching users", e)
            return emptyList()
        }
    }

    suspend fun getTests(testSetId: String?): List<Test> {
        if (testSetId != null) {
            if (testSetId.isEmpty()) return emptyList()
        }
        val snapshot = testSetId?.let {
            firestore.collection("testSets")
                .document(it)
                .collection("tests")
                .get()
                .await()
        }
        if (snapshot != null) {
            return snapshot.documents.map { it.toObject(Test::class.java)!! }
        }
        //TODO controlla bene questa funzione
        return emptyList()
    }
}