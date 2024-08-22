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
     * Retrieves a Test with a specific primary key.
     *
     * @param id The primary key of the Test to retrieve.
     * @return The Test object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM tests WHERE id = :id")
    suspend fun getTestById(id: String): Test?

    /**
     * Fetches all tests for a specific test set ID.
     * @param testSetId: The ID of the test set to fetch tests for.
     * @return A list of tests.
     */
    suspend fun getTests(testSetId: String?): List<Test>
    
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
}
