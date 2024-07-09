package com.amitranofinzi.vimata.data.model

import java.sql.Timestamp

data class Message (
    val chatId: String,
    val senderId: String = "",
    var text: String = "",
    val timeStamp: Timestamp,
    val receiverId: String = ""
) {
    constructor() : this(
        chatId = "",
        senderId = "",
        text = "",
        timeStamp = Timestamp(System.currentTimeMillis()),
        receiverId = ""
    )}


