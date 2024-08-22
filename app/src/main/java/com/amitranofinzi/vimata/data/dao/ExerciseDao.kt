package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amitranofinzi.vimata.data.model.Exercise

@Dao
interface ExerciseDao {

    /**
     * Retrieves an Exercise with a specific primary key.
     *
     * @param id The primary key of the Exercise to retrieve.
     * @return The Exercise object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getExerciseById(id: String): Exercise?

    /**
     * Fetches all exercises for a specific collection ID.
     * @param collectionID: The ID of the collection to fetch exercises for.
     * @return A list of exercises.
     */
    suspend fun getExercisesByCollectionId(collectionID: String): List<Exercise>

    /**
     * Fetches all exercises for a specific trainer ID.
     * @param trainerID: The ID of the trainer to fetch exercises for.
     * @return A list of exercises.
     */
    suspend fun getExercisesByTrainerId(trainerID: String): List<Exercise>


    /**
     * Inserts an Exercise into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param exercise The Exercise object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)

    /**
     * Inserts a list of Exercises into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param exercises The List of Exercises object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<Exercise>)


    /**
     * Updates an existing Exercise in the database.
     *
     * @param exercise The Exercise object to update.
     */
    @Update
    suspend fun update(exercise: Exercise)
}
