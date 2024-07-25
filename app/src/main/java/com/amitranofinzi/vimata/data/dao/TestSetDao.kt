package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amitranofinzi.vimata.data.model.TestSet

@Dao
interface TestSetDao {

    /**
     * Retrieves a list of TestSet where the value of a specific field equals a given value.
     *
     * @param field The name of the field to be compared.
     * @param value The value to be compared with the specified field.
     * @return A list of TestSet objects that meet the equality condition.
     */
    @Query("SELECT * FROM testSets WHERE :field = :value")
    fun getWhereEqual(field: String, value: String): List<TestSet>

    /**
     * Retrieves a list of TestSet where the value of a specific field is in a list of values.
     *
     * @param field The name of the field to be compared.
     * @param values The list of values to be compared with the specified field.
     * @return A list of TestSet objects that meet the inclusion condition.
     */
    @Query("SELECT * FROM testSets WHERE :field IN (:values)")
    fun getWhereIn(field: String, values: List<String>): List<TestSet>

    /**
     * Retrieves a TestSet with a specific primary key.
     *
     * @param id The primary key of the TestSet to retrieve.
     * @return The TestSet object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM testSets WHERE id = :id")
    fun getWithPrimaryKey(id: String): TestSet?

    /**
     * Inserts a TestSet into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param testSet The TestSet object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(testSet: TestSet)

    /**
     * Updates an existing TestSet in the database.
     *
     * @param testSet The TestSet object to update.
     */
    @Update
    fun update(testSet: TestSet)
}
