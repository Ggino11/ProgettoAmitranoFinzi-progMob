package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amitranofinzi.vimata.data.model.Workout

@Dao
interface WorkoutDao {

    /**
     * Retrieves a list of Workout where the value of a specific field equals a given value.
     *
     * @param field The name of the field to be compared.
     * @param value The value to be compared with the specified field.
     * @return A list of Workout objects that meet the equality condition.
     */
    @Query("SELECT * FROM workouts WHERE :field = :value")
    suspend fun getWhereEqual(field: String, value: String): List<Workout>

    /**
     * Retrieves a list of Workout where the value of a specific field is in a list of values.
     *
     * @param field The name of the field to be compared.
     * @param values The list of values to be compared with the specified field.
     * @return A list of Workout objects that meet the inclusion condition.
     */
    @Query("SELECT * FROM workouts WHERE :field IN (:values)")
    suspend fun getWhereIn(field: String, values: List<String>): List<Workout>

    /**
     * Retrieves a Workout with a specific primary key.
     *
     * @param id The primary key of the Workout to retrieve.
     * @return The Workout object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM workouts WHERE id = :id")
    suspend fun getWithPrimaryKey(id: String): Workout?

    /**
     * Inserts a Workout into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param workout The Workout object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: Workout)

    /**
     * Inserts a list of Workouts into the database.
     *
     * @param workouts The list of Workout objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<Workout>)

    /**
     * Updates an existing Workout in the database.
     *
     * @param workout The Workout object to update.
     */
    @Update
    suspend fun update(workout: Workout)
}