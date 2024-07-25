package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amitranofinzi.vimata.data.model.Chat

@Dao
interface ChatDao {

    /**
     * Retrieves a list of Chat where the value of a specific field equals a given value.
     *
     * @param field The name of the field to be compared.
     * @param value The value to be compared with the specified field.
     * @return A list of Chat objects that meet the equality condition.
     */
    @Query("SELECT * FROM chats WHERE :field = :value")
    fun getWhereEqual(field: String, value: String): List<Chat>

    /**
     * Retrieves a list of Chat where the value of a specific field is in a list of values.
     *
     * @param field The name of the field to be compared.
     * @param values The list of values to be compared with the specified field.
     * @return A list of Chat objects that meet the inclusion condition.
     */
    @Query("SELECT * FROM chats WHERE :field IN (:values)")
    fun getWhereIn(field: String, values: List<String>): List<Chat>

    /**
     * Retrieves a Chat with a specific primary key.
     *
     * @param chatId The primary key of the Chat to retrieve.
     * @return The Chat object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM chats WHERE chatId = :chatId")
    fun getWithPrimaryKey(chatId: String): Chat?

    /**
     * Inserts a Chat into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param chat The Chat object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chat: Chat)

    /**
     * Updates an existing Chat in the database.
     *
     * @param chat The Chat object to update.
     */
    @Update
    fun update(chat: Chat)
}
