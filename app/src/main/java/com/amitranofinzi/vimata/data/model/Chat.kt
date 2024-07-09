package com.amitranofinzi.vimata.data.model

data class Chat (
    val chatId: String,
    val relationshipID: String,
    val lastMessage: String //da cambiare per ora cosi per semplicita
) {
    constructor() : this(
        chatId = "",
        relationshipID = "",
        lastMessage = ""
    )
}