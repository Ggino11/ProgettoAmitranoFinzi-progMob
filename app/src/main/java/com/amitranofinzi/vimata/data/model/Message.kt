package com.amitranofinzi.vimata.data.model

import com.google.firebase.Timestamp


data class Message (
    val chatId: String = "",
    val senderId: String = "",
    var text: String = "",
    val timeStamp: Timestamp,
    val receiverId: String = "",
    val id: String = ""
) {
    constructor() : this(
        chatId = "",
        senderId = "",
        text = "",
        timeStamp = Timestamp.now(),
        receiverId = "",
        id = "",
    )}


