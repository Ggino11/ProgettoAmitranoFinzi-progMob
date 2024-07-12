package com.amitranofinzi.vimata.data.model

data class Exercise(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val videoUrl: String = "",
    val trainerID: String = "",
    val collectionID: String = ""

){  constructor() : this(
    id = "",
    name = "",
    description = "",
    videoUrl = "",
    trainerID = "",
    collectionID= ""
)
}