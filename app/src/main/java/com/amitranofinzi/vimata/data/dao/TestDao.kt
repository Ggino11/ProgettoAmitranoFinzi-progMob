package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amitranofinzi.vimata.data.extensions.TestStatus
import com.amitranofinzi.vimata.data.model.Test

@Dao
interface TestDao {

    /**
     * Retrieves a Test with a specific primary key.
     *
     * @param id The primary key of the Test to retrieve.
     * @return The Test object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM tests WHERE id = :id")
    suspend fun getTestById(id: String): Test?

    /**
     * Retrieves a list of Test entities by test set ID.
     *
     * @param testSetId The ID of the test set whose tests are to be retrieved.
     * @return A list of Test objects associated with the specified test set ID.
     */
    @Query("SELECT * FROM tests WHERE testSetID = :testSetId")
    suspend fun getTestsByTestSetId(testSetId: String): List<Test>
    
    /**
     * Inserts a Test into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param test The Test object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(test: Test)

    /**
     * Inserts a list of Tests into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param tests The list of tests to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tests: List<Test>)

    /**
     * Updates an existing Test in the database.
     *
     * @param test The Test object to update.
     */
    @Update
    suspend fun update(test: Test)

    /**
     * Updates the result of a Test entity in the database.
     *
     * @param testId The ID of the Test to be updated.
     * @param result The new result to set for the Test.
     */
    @Query("UPDATE tests SET result = :result WHERE id = :testId")
    suspend fun updateTestResult(testId: String, result: Double)



    /**
     * Updates the status of a Test entity in the database.
     *
     * @param testId The ID of the Test to be updated.
     * @param status The new status to set for the Test.
     */
    @Query("UPDATE tests SET status = :status WHERE id = :testId")
    suspend fun updateTestStatus(testId: String, status: TestStatus)
}
