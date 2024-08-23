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
     * Retrieves a TestSet with a specific primary key.
     *
     * @param id The primary key of the TestSet to retrieve.
     * @return The TestSet object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM testSets WHERE id = :id")
    suspend fun getTestSetById(id: String): TestSet?

    /**
     * Retrieves a list of TestSet entities by athlete ID.
     *
     * @param athleteId The ID of the athlete whose test sets are to be retrieved.
     * @return A list of TestSet objects associated with the specified athlete ID.
     */
    @Query("SELECT * FROM testSets WHERE athleteID = :athleteId")
    suspend fun getTestSetsByAthleteId(athleteId: String): List<TestSet>

    /**
     * Inserts a TestSet into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param testSet The TestSet object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(testSet: TestSet)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(testSets: List<TestSet>)

    /**
     * Updates an existing TestSet in the database.
     *
     * @param testSet The TestSet object to update.
     */
    @Update
    suspend fun update(testSet: TestSet)
}
