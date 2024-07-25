package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amitranofinzi.vimata.data.model.Message

@Dao
interface MessageDao {

    /**
     * Retrieves a list of Message where the value of a specific field equals a given value.
     *
     * @param field The name of the field to be compared.
     * @param value The value to be compared with the specified field.
     * @return A list of Message objects that meet the equality condition.
     */
    @Query("SELECT * FROM messages WHERE :field = :value")
    fun getWhereEqual(field: String, value: String): List<Message>

    /**
     * Retrieves a list of Message where the value of a specific field is in a list of values.
     *
     * @param field The name of the field to be compared.
     * @param values The list of values to be compared with the specified field.
     * @return A list of Message objects that meet the inclusion condition.
     */
    @Query("SELECT * FROM messages WHERE :field IN (:values)")
    fun getWhereIn(field: String, values: List<String>): List<Message>

    /**
     * Retrieves a Message with a specific primary key.
     *
     * @param id The primary key of the Message to retrieve.
     * @return The Message object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM messages WHERE id = :id")
    fun getWithPrimaryKey(id: String): Message?

    /**
     * Inserts a Message into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param message The Message object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message)

    /**
     * Updates an existing Message in the database.
     *
     * @param message The Message object to update.
     */
    @Update
    fun update(message: Message)

    /**
     * Retrieves a list of Message objects ordered by a specific field.
     *
     * @param field The name of the field to order by.
     * @param ascending Whether the order should be ascending (true) or descending (false).
     * @return A list of Message objects ordered by the specified field.
     */
    @Query("SELECT * FROM messages ORDER BY CASE WHEN :ascending = 1 THEN :field END ASC, CASE WHEN :ascending = 0 THEN :field END DESC")
    fun orderBy(field: String, ascending: Boolean): List<Message>
}
