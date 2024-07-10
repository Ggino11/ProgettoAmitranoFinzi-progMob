package com.amitranofinzi.vimata.data.model

data class Collection(
    val id: String = "",
    val title: String = "",
    val trainerID: String = "",
){  constructor() : this(
    id = "",
    title = "",
    trainerID = ""
)
}