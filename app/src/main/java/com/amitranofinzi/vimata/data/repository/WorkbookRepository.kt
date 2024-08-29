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

/**
 * Repository for managing collections, exercises, and workouts.
 * It interacts with Firebase Firestore and the local Room database to synchronize data.
 */
class WorkbookRepository(
    private val collectionDao: CollectionDao,
    private val exerciseDao: ExerciseDao,
    private val workoutDao: WorkoutDao,
    private val context: Context
) {

    private val firestore = FirebaseFirestore.getInstance()

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
     * Retrieves a list of collections associated with a specific trainer.
     *
     * @param trainerID ID of the trainer.
     * @return A list of collections.
     */
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

    /**
     * Retrieves a list of exercises associated with a specific collection.
     *
     * @param collectionID ID of the collection.
     * @return A list of exercises.
     */
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

    /**
     * Adds a new collection to Firestore and the local database.
     *
     * @param collection The collection to add.
     */
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

    /**
     * Retrieves a specific collection by its ID.
     *
     * @param collectionID ID of the collection.
     * @return The collection object.
     */
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

    /**
     * Uploads a new exercise to Firestore and caches it in the local database.
     *
     * @param exercise The exercise to upload.
     */
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

                    exerciseDao.insert(exerciseWithId)

                    Log.d("WorkbookRepository", "Exercise uploaded successfully with ID: ${result.id}")
                } else {
                    Log.d("WorkbookRepository", "Network unavailable, skipping Firestore operation")
                    exerciseDao.insert(exercise)
                }
            } catch (e: Exception) {
                Log.e("WorkbookRepository", "Error uploading exercise", e)
            }
        }
    }

    /**
     * Retrieves a list of exercises associated with a specific trainer.
     *
     * @param trainerID ID of the trainer.
     * @return A list of exercises.
     */
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

    /**
     * Uploads a new workout to Firestore and caches it in the local database.
     *
     * @param workout The workout to upload.
     */
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