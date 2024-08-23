package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amitranofinzi.vimata.data.model.Relationship

@Dao
interface RelationshipDao {

    /**
     * Retrieves a list of Relationship where the value of a specific field equals a given value.
     *
     * @param field The name of the field to be compared.
     * @param value The value to be compared with the specified field.
     * @return A list of Relationship objects that meet the equality condition.
     */
    @Query("SELECT * FROM relationships WHERE :field = :value")
    suspend fun getWhereEqual(field: String, value: String): List<Relationship>

    /**
     * Retrieves a list of Relationship where the value of a specific field is in a list of values.
     *
     * @param field The name of the field to be compared.
     * @param values The list of values to be compared with the specified field.
     * @return A list of Relationship objects that meet the inclusion condition.
     */
    @Query("SELECT * FROM relationships WHERE :field IN (:values)")
    suspend fun getWhereIn(field: String, values: List<String>): List<Relationship>

    @Query("SELECT * FROM relationships WHERE athleteID = :value")
    suspend fun getWhereAthleteID(value: String): List<Relationship>


    /**
     * Retrieves a Relationship with a specific primary key.
     *
     * @param id The primary key of the Relationship to retrieve.
     * @return The Relationship object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM relationships WHERE id = :id")
    suspend fun getWithPrimaryKey(id: String): Relationship?


    /**
     * Retrieves all relationships where the athleteID matches the given value.
     *
     * @param athleteID The ID of the athlete to filter by.
     * @return A list of Relationship objects where athleteID matches the provided value.
     */
    @Query("SELECT * FROM relationships WHERE athleteID = :athleteID")
    suspend fun getRelationshipsByAthleteId(athleteID: String): List<Relationship>

    /**
     * Retrieves a specific relationship by its ID.
     *
     * @param relationshipId ID of the relationship to retrieve.
     * @return The relationship with the given ID, or null if not found.
     */
    @Query("SELECT * FROM relationships WHERE id = :relationshipId LIMIT 1")
    suspend fun getRelationshipById(relationshipId: String): Relationship?

    /**
     * Retrieves a list of relationships for a specific user based on their ID and user type.
     *
     * @param userId The ID of the user to filter relationships.
     * @param userType The type of the user ("athlete" or "trainer").
     * @return List of relationships for the user.
     */
    @Query("SELECT * FROM relationships WHERE (:userType = 'athlete' AND athleteID = :userId) OR (:userType = 'trainer' AND trainerID = :userId)")
    suspend fun getRelationshipsByUserId(userId: String, userType: String): List<Relationship>


    /**
     * Retrieves all relationships where the trainerID matches the given value.
     *
     * @param trainerID The ID of the trainer to filter by.
     * @return A list of Relationship objects where trainerID matches the provided value.
     */
    @Query("SELECT * FROM relationships WHERE trainerID = :trainerID")
    suspend fun getRelationshipsByTrainerId(trainerID: String): List<Relationship>

    /**
     * Inserts a Relationship into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param relationship The Relationship object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(relationship: Relationship)

    /**
     * Inserts a list of Relationships into the database.
     *
     * @param relationships The list of Relationship objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(relationships: List<Relationship>)

    /**
     * Updates an existing Relationship in the database.
     *
     * @param relationship The Relationship object to update.
     */
    @Update
    suspend fun update(relationship: Relationship)
}
