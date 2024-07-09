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
        Log.d("getTests", "Called with testSetId: $testSetId")

        if (testSetId == null) {
            Log.d("getTests", "testSetId is null")
            return emptyList()
        }

        if (testSetId.isEmpty()) {
            Log.d("getTests", "testSetId is empty")
            return emptyList()
        }

        return try {
            // Retrieve the testSet document
            val testSetDocument = firestore.collection("testSets")
                .document(testSetId)
                .get()
                .await()

            if (!testSetDocument.exists()) {
                Log.d("getTests", "TestSet document does not exist")
                return emptyList()
            }

            val testSet = testSetDocument.toObject(TestSet::class.java)
            if (testSet == null) {
                Log.d("getTests", "Failed to convert document to TestSet")
                return emptyList()
            }

            val testIds = testSet.testIDs
            if (testIds.isEmpty()) {
                Log.d("getTests", "TestSet has no testIDs")
                return emptyList()
            }

            Log.d("getTests", "Fetching tests with IDs: $testIds")

            // Retrieve the tests based on the test IDs
            val snapshot = firestore.collection("tests")
                .whereIn("id", testIds)
                .get()
                .await()

            if (snapshot.isEmpty) {
                Log.d("getTests", "No documents found in the snapshot")
                return emptyList()
            }

            val tests = snapshot.documents.mapNotNull { document ->
                val test = document.toObject(Test::class.java)
                if (test == null) {
                    Log.d("getTests", "Document ${document.id} could not be converted to Test")
                }
                test
            }

            Log.d("getTests", "Fetched ${tests.size} tests")
            tests
        } catch (e: Exception) {
            Log.e("getTests", "Error fetching tests", e)
            emptyList()
        }
    }
}