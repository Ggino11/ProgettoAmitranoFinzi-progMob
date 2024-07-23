package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity(tableName = "messages")
data class Message (
    val chatId: String = "",
    val senderId: String = "",
    var text: String = "",
    val timeStamp: Timestamp,
    val receiverId: String = "",
    @PrimaryKey val id: String = ""
) {
    constructor() : this(
        chatId = "",
        senderId = "",
        text = "",
        timeStamp = Timestamp.now(),
        receiverId = "",
        id = "",
    )}


