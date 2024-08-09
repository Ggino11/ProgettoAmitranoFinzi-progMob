package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amitranofinzi.vimata.data.model.Test

@Dao
interface TestDao {

    /**
     * Retrieves a list of Test where the value of a specific field equals a given value.
     *
     * @param field The name of the field to be compared.
     * @param value The value to be compared with the specified field.
     * @return A list of Test objects that meet the equality condition.
     */
    @Query("SELECT * FROM tests WHERE :field = :value")
    suspend fun getWhereEqual(field: String, value: String): List<Test>

    /**
     * Retrieves a list of Test where the value of a specific field is in a list of values.
     *
     * @param field The name of the field to be compared.
     * @param values The list of values to be compared with the specified field.
     * @return A list of Test objects that meet the inclusion condition.
     */
    @Query("SELECT * FROM tests WHERE :field IN (:values)")
    suspend fun getWhereIn(field: String, values: List<String>): List<Test>

    /**
     * Retrieves a Test with a specific primary key.
     *
     * @param id The primary key of the Test to retrieve.
     * @return The Test object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM tests WHERE id = :id")
    suspend fun getWithPrimaryKey(id: String): Test?

    /**
     * Inserts a Test into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param test The Test object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(test: Test)

    /**
     * Updates an existing Test in the database.
     *
     * @param test The Test object to update.
     */
    @Update
    suspend fun update(test: Test)
}
