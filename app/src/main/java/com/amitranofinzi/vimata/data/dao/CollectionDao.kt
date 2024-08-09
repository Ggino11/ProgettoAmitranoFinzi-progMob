package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amitranofinzi.vimata.data.model.Collection

@Dao
interface CollectionDao {

    /**
     * Retrieves a list of Collection where the value of a specific field equals a given value.
     *
     * @param field The name of the field to be compared.
     * @param value The value to be compared with the specified field.
     * @return A list of Collection objects that meet the equality condition.
     */
    @Query("SELECT * FROM collections WHERE :field = :value")
    suspend fun getWhereEqual(field: String, value: String): List<Collection>

    /**
     * Retrieves a list of Collection where the value of a specific field is in a list of values.
     *
     * @param field The name of the field to be compared.
     * @param values The list of values to be compared with the specified field.
     * @return A list of Collection objects that meet the inclusion condition.
     */
    @Query("SELECT * FROM collections WHERE :field IN (:values)")
    suspend fun getWhereIn(field: String, values: List<String>): List<Collection>

    /**
     * Retrieves a Collection with a specific primary key.
     *
     * @param id The primary key of the Collection to retrieve.
     * @return The Collection object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM collections WHERE id = :id")
    suspend fun getWithPrimaryKey(id: String): Collection?

    /**
     * Inserts a Collection into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param collection The Collection object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(collection: Collection)

    /**
     * Updates an existing Collection in the database.
     *
     * @param collection The Collection object to update.
     */
    @Update
    suspend fun update(collection: Collection)
}
