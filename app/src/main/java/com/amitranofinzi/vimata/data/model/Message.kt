package com.amitranofinzi.vimata.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId


/**
 * Room entity annotated for database table creation.
 * The `messages` table has two foreign keys with the `users` and `chats` tables.
 *
 * @property chatId The ID of the chat to which this message belongs. And the firebase document ID
 * @property senderId The ID of the user who sent the message.
 * @property text The content of the message.
 * @property timeStamp The time when the message was sent.
 * @property receiverId The ID of the user who is the intended recipient of the message.
 * @property id The unique ID of the message.
 */
@Entity(tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["receiverId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Chat::class,
            parentColumns = ["chatId"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )],
        indices = [Index(value = ["receiverId"]), Index(value = ["senderId"]), Index(value = ["chatId"]) ] // Aggiungi un indice qui

)
data class Message (
    val chatId: String = "",
    val senderId: String = "",
    var text: String = "",
    val timeStamp: String = "",
    val receiverId: String = "",
    @PrimaryKey @DocumentId @NonNull val id: String = ""
) {
    constructor() : this(
        chatId = "",
        senderId = "",
        text = "",
        timeStamp = Timestamp.now().toString(),
        receiverId = "",
        id = "",
    )}


