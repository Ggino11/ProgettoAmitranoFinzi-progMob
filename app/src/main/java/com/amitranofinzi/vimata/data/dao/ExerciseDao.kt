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
     * Retrieves a list of Exercise where the value of a specific field equals a given value.
     *
     * @param field The name of the field to be compared.
     * @param value The value to be compared with the specified field.
     * @return A list of Exercise objects that meet the equality condition.
     */
    @Query("SELECT * FROM exercises WHERE :field = :value")
    fun getWhereEqual(field: String, value: String): List<Exercise>

    /**
     * Retrieves a list of Exercise where the value of a specific field is in a list of values.
     *
     * @param field The name of the field to be compared.
     * @param values The list of values to be compared with the specified field.
     * @return A list of Exercise objects that meet the inclusion condition.
     */
    @Query("SELECT * FROM exercises WHERE :field IN (:values)")
    fun getWhereIn(field: String, values: List<String>): List<Exercise>

    /**
     * Retrieves an Exercise with a specific primary key.
     *
     * @param id The primary key of the Exercise to retrieve.
     * @return The Exercise object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM exercises WHERE id = :id")
    fun getWithPrimaryKey(id: String): Exercise?

    /**
     * Inserts an Exercise into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param exercise The Exercise object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exercise: Exercise)

    /**
     * Updates an existing Exercise in the database.
     *
     * @param exercise The Exercise object to update.
     */
    @Update
    fun update(exercise: Exercise)
}
