package com.amitranofinzi.vimata.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.amitranofinzi.vimata.data.dao.CollectionDao
import com.amitranofinzi.vimata.data.dao.ExerciseDao
import com.amitranofinzi.vimata.data.dao.WorkoutDao
import com.amitranofinzi.vimata.data.model.Collection
import com.amitranofinzi.vimata.data.model.Exercise
import com.amitranofinzi.vimata.data.model.Workout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class WorkbookRepository(
    private val collectionDao: CollectionDao,
    private val exerciseDao: ExerciseDao,
    private val workoutDao: WorkoutDao,
    private val context: Context
) {

    private val firestore = FirebaseFirestore.getInstance()
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
    suspend fun getCollections(trainerID: String): List<Collection> {
        return withContext(Dispatchers.IO) {
            if (isNetworkAvailable()) {
                try {
                    val snapshot = firestore.collection("collections")
                        .whereEqualTo("trainerID", trainerID)
                        .get()
                        .await()

                    val collections = snapshot.documents.mapNotNull { it.toObject(Collection::class.java) }

                    // Cache in Room
                    collectionDao.insertAll(collections)

                    collections
                } catch (e: Exception) {
                    Log.e("WorkbookRepository", "Error fetching collections from Firestore", e)
                    // Fallback to Room
                    collectionDao.getCollectionsByTrainerId(trainerID)
                }
            } else {
                // Fallback to Room
                collectionDao.getCollectionsByTrainerId(trainerID)
            }
        }
    }

    suspend fun getExercises(collectionID: String?): List<Exercise> {
        return if (collectionID != null) {
            withContext(Dispatchers.IO) {
                if (isNetworkAvailable()) {
                    try {
                        val snapshot = firestore.collection("workbook")
                            .whereEqualTo("collectionID", collectionID)
                            .get()
                            .await()

                        val exercises = snapshot.documents.mapNotNull { it.toObject(Exercise::class.java) }

                        // Cache in Room
                        exerciseDao.insertAll(exercises)

                        exercises
                    } catch (e: Exception) {
                        Log.e("WorkbookRepository", "Error fetching exercises from Firestore", e)
                        // Fallback to Room
                        exerciseDao.getExercisesByCollectionID(collectionID)
                    }
                } else {
                    // Fallback to Room
                    exerciseDao.getExercisesByCollectionID(collectionID)
                }
            }
        } else {
            Log.d("WorkbookRepository", "CollectionID is null")
            emptyList()
        }
    }

    suspend fun addCollection(collection: Collection) {
        withContext(Dispatchers.IO) {
            try {
                if (isNetworkAvailable()) {
                    firestore.collection("collections").add(collection).await()
                    // Cache in Room
                    collectionDao.insert(collection)
                } else {
                    Log.d("WorkbookRepository", "Network unavailable, skipping Firestore operation")
                    // Cache in Room anyway
                    collectionDao.insert(collection)
                }
            } catch (e: Exception) {
                Log.e("WorkbookRepository", "Error adding collection", e)
            }
        }
    }

    suspend fun getCollectionByID(collectionID: String): Collection {
        return withContext(Dispatchers.IO) {
            (if (isNetworkAvailable()) {
                try {
                    val document = firestore.collection("collections")
                        .document(collectionID)
                        .get()
                        .await()

                    val collection = document.toObject(Collection::class.java)

                    // Cache in Room if found
                    if (collection != null) {
                        collectionDao.insert(collection)
                    }

                    collection
                } catch (e: Exception) {
                    Log.e("WorkbookRepository", "Error fetching collection by ID from Firestore", e)
                    // Fallback to Room
                    collectionDao.getCollectionById(collectionID)
                }
            } else {
                // Fallback to Room
                collectionDao.getCollectionById(collectionID)
            })!!
        }
    }

    suspend fun uploadExercise(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            try {
                if (isNetworkAvailable()) {
                    val result = firestore.collection("workbook")
                        .add(exercise)
                        .await()

                    // Assign ID and update Firestore
                    val exerciseWithId = exercise.copy(id = result.id)
                    firestore.collection("workbook")
                        .document(result.id)
                        .set(exerciseWithId)
                        .await()

                    // Cache in Room
                    exerciseDao.insert(exerciseWithId)

                    Log.d("WorkbookRepository", "Exercise uploaded successfully with ID: ${result.id}")
                } else {
                    Log.d("WorkbookRepository", "Network unavailable, skipping Firestore operation")
                    // Cache in Room anyway
                    exerciseDao.insert(exercise)
                }
            } catch (e: Exception) {
                Log.e("WorkbookRepository", "Error uploading exercise", e)
            }
        }
    }

    suspend fun getExercisesByTrainerId(trainerID: String): List<Exercise> {
        return withContext(Dispatchers.IO) {
            if (isNetworkAvailable()) {
                try {
                    val snapshot = firestore.collection("workbook")
                        .whereEqualTo("trainerID", trainerID)
                        .get()
                        .await()

                    val exercises = snapshot.documents.mapNotNull { it.toObject(Exercise::class.java) }

                    // Cache in Room
                    exerciseDao.insertAll(exercises)

                    exercises
                } catch (e: Exception) {
                    Log.e("WorkbookRepository", "Error fetching exercises by trainer ID from Firestore", e)
                    // Fallback to Room
                    exerciseDao.getExercisesByTrainerID(trainerID)
                }
            } else {
                // Fallback to Room
                exerciseDao.getExercisesByTrainerID(trainerID)
            }
        }
    }

    suspend fun uploadWorkout(workout: Workout) {
        withContext(Dispatchers.IO) {
            try {
                if (isNetworkAvailable()) {
                    val result = firestore.collection("workouts")
                        .add(workout)
                        .await()

                    // Assign ID and update Firestore
                    val workoutWithId = workout.copy(id = result.id)
                    firestore.collection("workouts")
                        .document(result.id)
                        .set(workoutWithId)
                        .await()

                    // Cache in Room
                    workoutDao.insert(workoutWithId)

                    Log.d("WorkbookRepository", "Workout uploaded successfully with ID: ${result.id}")
                } else {
                    Log.d("WorkbookRepository", "Network unavailable, skipping Firestore operation")
                    // Cache in Room anyway
                    workoutDao.insert(workout)
                }
            } catch (e: Exception) {
                Log.e("WorkbookRepository", "Error uploading workout", e)
            }
        }
    }
}