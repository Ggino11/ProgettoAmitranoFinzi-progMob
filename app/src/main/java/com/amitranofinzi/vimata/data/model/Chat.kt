package com.amitranofinzi.vimata.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a chat session in the application.
 * This is a Room entity annotated for database table creation.
 * The `chats` table has a foreign key relationship with the `relationships` table.
 *
 * @property chatId The unique ID of the chat. And the firebase document ID
 * @property lastMessage The content of the last message in the chat.
 * @property relationshipID The ID of the relationship associated with this chat.
 */
@Entity(
    tableName = "chats",
    foreignKeys = [
        ForeignKey(
            entity = Relationship::class,
            parentColumns = ["id"],
            childColumns = ["relationshipID"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["relationshipID"])]
    )
data class Chat (
    @PrimaryKey @NonNull val chatId: String,
    val relationshipID: String,
    val lastMessage: String
) {
    constructor() : this(
        chatId = "",
        relationshipID = "",
        lastMessage = ""
    )
}