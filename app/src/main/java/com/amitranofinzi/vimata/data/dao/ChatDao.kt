package com.amitranofinzi.vimata.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatDao {
    /**
     * Retrieves chats by their relationship ID as a Flow.
     *
     * @param relationshipId The relationship ID to query.
     * @return A Flow emitting the list of chats.
     */
    @Query("SELECT * FROM chats WHERE relationshipID = :relationshipId")
    fun getChatsByRelationshipId(relationshipId: String): List<Chat>

    /**
     * Inserts a chat into the database.
     *
     * @param chat The chat to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chat)

    /**
     * Retrieves chats by a list of relationship IDs.
     *
     * @param relationshipIds The list of relationship IDs to query.
     * @return A list of Chat objects.
     */
    @Query("SELECT * FROM chats WHERE relationshipID IN (:relationshipIds)")
    suspend fun getChatsByRelationshipIds(relationshipIds: List<String>): List<Chat>

    /**
     * Retrieves a chat by its ID.
     *
     * @param chatId The ID of the chat to retrieve.
     * @return The Chat object if found, null otherwise.
     */
    @Query("SELECT * FROM chats WHERE chatId = :chatId LIMIT 1")
    suspend fun getChatById(chatId: String): Chat?

    // MESSAGE FUNCTIONS, {TODO: make dao for message}
    //bisogna capire cosa quale tra le due opzioni è più adatta

    /**
     * Retrieves messages by their chat ID as a Flow.
     *
     * @param chatId The chat ID to query.
     * @return A Flow emitting the list of messages.
     */
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timeStamp")
    fun getMessagesByChatId(chatId: String): Flow<List<Message>>

    /**
     * Inserts a message into the database.
     *
     * @param message The message to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)
}