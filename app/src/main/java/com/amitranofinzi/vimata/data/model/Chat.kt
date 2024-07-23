package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Chat
@Entity(tableName = "chats"  )
data class Chat (
    @PrimaryKey val chatId: String,
    val relationshipID: String,
    val lastMessage: String //da cambiare per ora cosi per semplicita
) {
    constructor() : this(
        chatId = "",
        relationshipID = "",
        lastMessage = ""
    )
}