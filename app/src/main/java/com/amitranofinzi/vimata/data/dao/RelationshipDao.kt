package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amitranofinzi.vimata.data.model.Relationship


@Dao
interface RelationshipDao {
    /**
     * Retrieves relationships by user ID.
     *
     * @param userId The user ID to query.
     * @return A list of Relationship objects.
     */
    @Query("SELECT * FROM relationships WHERE athleteID = :userId OR trainerID = :userId")
    suspend fun getRelationshipsByUserId(userId: String): List<Relationship>

    /**
     * Inserts a relationship into the database.
     *
     * @param relationship The relationship to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelationship(relationship: Relationship)

    /**
     * Retrieves a relationship by its ID.
     *
     * @param relationshipId The ID of the relationship to retrieve.
     * @return The Relationship object if found, null otherwise.
     */
    @Query("SELECT * FROM relationships WHERE id = :relationshipId LIMIT 1")
    suspend fun getRelationshipById(relationshipId: String): Relationship?
}