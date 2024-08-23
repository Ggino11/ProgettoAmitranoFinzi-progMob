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
     * Retrieves a list of all chats.
     *
     * @return A list of all Chat objects in the database.
     */
    @Query("SELECT * FROM chats")
    suspend fun getAll(): List<Chat>

    /**
     * Retrieves a chat from the database by its unique ID.
     *
     * @param chatId The unique ID of the chat to be fetched.
     * @return The Chat object with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM chats WHERE chatId = :chatId")
    suspend fun getChatById(chatId: String): Chat?


    /**
     * Retrieves a list of chats based on a list of relationship IDs.
     *
     * @param relationshipIDs List of relationship IDs to filter chats.
     * @return List of chats matching the given relationship IDs.
     */
    @Query("SELECT * FROM chats WHERE relationshipID IN (:relationshipIDs)")
    suspend fun getChatsByRelationshipIDs(relationshipIDs: List<String>): List<Chat>


    /**
     * Inserts a Chat into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param chat The Chat object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chat: Chat)

    /**
     * Inserts a list of Chats into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param chats The List of Chat objects to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chats: List<Chat>)

    /**
     * Updates an existing Chat in the database.
     *
     * @param chat The Chat object to update.
     */
    @Update
    suspend fun update(chat: Chat)

    /**
     * Updates the last message in a specific chat.
     *
     * @param chatId ID of the chat to update.
     * @param lastMessage The new last message.
     */
    @Query("UPDATE chats SET lastMessage = :lastMessage WHERE chatId = :chatId")
    suspend fun updateLastMessage(chatId: String, lastMessage: String)

    /**
     * Deletes a Chat with a specific primary key (chatId).
     *
     * @param chatId The primary key of the Chat to delete.
     */
    @Query("DELETE FROM chats WHERE chatId = :chatId")
    suspend fun deleteByChatId(chatId: String)

    /**
     * Deletes all Chats from the database.
     */
    @Query("DELETE FROM chats")
    suspend fun clearAll()
}
