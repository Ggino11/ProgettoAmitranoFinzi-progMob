package com.amitranofinzi.vimata.data.model

data class Exercise(
    val name: String = "",
    val description: String = "",
    val videoUrl: String = "",
    val trainerID: String = ""

){  constructor() : this(
    name = "",
    description = "",
    videoUrl = "",
    trainerID = ""
)
}