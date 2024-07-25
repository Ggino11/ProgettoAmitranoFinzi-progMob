package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

/**
 * Represents a chat session in the application.
 * This is a Room entity annotated for database table creation.
 * The `chats` table has a foreign key relationship with the `relationships` table.
 *
 * @property chatId The unique ID of the chat.
 * @property lastMessage The content of the last message in the chat.
 * @property relationshipID The ID of the relationship associated with this chat.
 */
@Entity(
    tableName = "chats",
    foreignKeys = [
        ForeignKey(
            entity = Relationship::class,
            parentColumns = ["relationshipId"],
            childColumns = ["relationshipId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Chat (
    @PrimaryKey @DocumentId val chatId: String,
    val relationshipID: String,
    val lastMessage: String
) {
    constructor() : this(
        chatId = "",
        relationshipID = "",
        lastMessage = ""
    )
}