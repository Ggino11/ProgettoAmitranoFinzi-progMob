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
    fun getWhereEqual(field: String, value: String): List<Relationship>

    /**
     * Retrieves a list of Relationship where the value of a specific field is in a list of values.
     *
     * @param field The name of the field to be compared.
     * @param values The list of values to be compared with the specified field.
     * @return A list of Relationship objects that meet the inclusion condition.
     */
    @Query("SELECT * FROM relationships WHERE :field IN (:values)")
    fun getWhereIn(field: String, values: List<String>): List<Relationship>

    /**
     * Retrieves a Relationship with a specific primary key.
     *
     * @param id The primary key of the Relationship to retrieve.
     * @return The Relationship object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM relationships WHERE id = :id")
    fun getWithPrimaryKey(id: String): Relationship?

    /**
     * Inserts a Relationship into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param relationship The Relationship object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(relationship: Relationship)

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
    fun update(relationship: Relationship)
}
