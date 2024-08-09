package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.amitranofinzi.vimata.data.model.Message

@Dao
interface MessageDao {

    /**
     * Retrieves messages by their chat ID as a Flow.
     *
     * @param chatId The chat ID to query.
     * @return A Flow emitting the list of messages.
     */
//    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timeStamp")
//    suspend fun getMessagesByChatId(chatId: String): Flow<List<Message>>

    /**
     * Inserts a message into the database.
     *
     * @param message The message to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)
}