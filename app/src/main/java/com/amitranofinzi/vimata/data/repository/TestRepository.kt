package com.amitranofinzi.vimata.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.amitranofinzi.vimata.data.dao.TestDao
import com.amitranofinzi.vimata.data.dao.TestSetDao
import com.amitranofinzi.vimata.data.model.Test
import com.amitranofinzi.vimata.data.model.TestSet
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repository for handling test-related operations, including fetching test sets, creating tests,
 * and updating test results or statuses. Integrates Firebase Firestore with local database.
 */
class TestRepository(
    private val testDao: TestDao,
    private val testSetDao: TestSetDao,
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
     * Retrieves the list of test sets for a specific athlete.
     *
     * @param athleteID ID of the athlete.
     * @return A list of test sets.
     */
    suspend fun getTestSetsForAthlete(athleteID: String): List<TestSet> {
        Log.d("TestRepository", "getTestSetsForAthlete called with athleteIds: $athleteID")

        return if (isNetworkAvailable()) {
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

                // Salva i testSet nel database locale
                testSetDao.insertAll(testSets)

                testSets
            } catch (e: Exception) {
                Log.e("TestRepository", "Error fetching test sets", e)
                emptyList()
            }
        } else {
            // Recupera i dati dal database locale
            testSetDao.getTestSetsByAthleteId(athleteID)
        }
    }

    /**
     * Retrieves the list of tests within a specific test set.
     *
     * @param testSetId ID of the test set.
     * @return A list of tests.
     */
    suspend fun getTests(testSetId: String?): List<Test> {
        Log.d("getTests", "Called with testSetId: $testSetId")

        if (testSetId == null || testSetId.isEmpty()) {
            Log.d("getTests", "testSetId is null or empty")
            return emptyList()
        }

        return if (isNetworkAvailable()) {
            try {
                Log.d("getTests", "Fetching tests with testSetId: $testSetId")

                val snapshot = firestore.collection("tests")
                    .whereEqualTo("testSetID", testSetId)
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

                testDao.insertAll(tests)

                tests
            } catch (e: Exception) {
                Log.e("getTests", "Error fetching tests", e)
                emptyList()
            }
        } else {
            testDao.getTestsByTestSetId(testSetId)
        }
    }

    /**
     * Updates the result of a specific test.
     *
     * @param test The test object containing the updated result.
     */
    suspend fun updateTestResult(test: Test) {
        try {
            if (isNetworkAvailable()) {
                Log.d("TestRepository", "Updating test result with testId: ${test.id}, new result: ${test.result}")
                firestore.collection("tests").document(test.id)
                    .update("result", test.result)
                    .await()
                Log.d("TestRepository", "Update successful")
            }

            // Aggiorna il test anche nel database locale
            testDao.updateTestResult(test.id, test.result)
        } catch (e: Exception) {
            Log.e("TestRepository", "Error updating test result: ${e.message}", e)
        }
    }

    /**
     * Updates the status of a specific test.
     *
     * @param test The test object containing the updated status.
     */
    suspend fun updateTestStatus(test: Test) {
        try {
            if (isNetworkAvailable()) {
                Log.d("TestRepository", "Updating test status with testId: ${test.id}, new status: ${test.status}")
                firestore.collection("tests").document(test.id)
                    .update("status", test.status)
                    .await()
                Log.d("TestRepository", "Update status successful")
            }

            // Aggiorna lo stato del test anche nel database locale
            testDao.updateTestStatus(test.id, test.status)
        } catch (e: Exception) {
            Log.e("TestRepository", "Error updating test status: ${e.message}", e)
        }
    }

    /**
     * Creates a new test set and stores it in both Firestore and the local database.
     *
     * @param testSet The test set to be created.
     * @return The ID of the newly created test set.
     */
    suspend fun createTestSet(testSet: TestSet): String {
        try {
            val firestore = FirebaseFirestore.getInstance()

            // Add testSet in Firestore
            val result = firestore.collection("testSets")
                .add(testSet)
                .await()

            val testSetId = result.id
            Log.d("TestRepository", "TestSet added successfully with ID: $testSetId")

            // Update testSet in Firestore to make sure id is correctly updated
            val updatedTestSet = testSet.copy(id = testSetId)
            firestore.collection("testSets")
                .document(testSetId)
                .set(updatedTestSet)
                .await()

            Log.d("TestRepository", "TestSet updated with ID: $testSetId")

            // Salva il testSet nel database locale
            testSetDao.insertAll(listOf(updatedTestSet))

            return testSetId
        } catch (e: Exception) {
            Log.e("TestRepository", "Error creating TestSet", e)
            return ""
        }
    }

    /**
     * Creates a new test and stores it in both Firestore and the local database.
     *
     * @param test The test to be created.
     */
    suspend fun createTest(test: Test) {
        try {
            val result = firestore.collection("tests")
                .add(test)
                .await()

            val testId = result.id
            val updatedTest = test.copy(id = testId)
            firestore.collection("tests")
                .document(testId)
                .set(updatedTest)
                .await()

            Log.d("TestRepository", "Test created successfully with ID: $testId")

            // Salva il test nel database locale
            testDao.insertAll(listOf(updatedTest))
        } catch (e: Exception) {
            Log.e("TestRepository", "Error creating Test", e)
        }
    }
}