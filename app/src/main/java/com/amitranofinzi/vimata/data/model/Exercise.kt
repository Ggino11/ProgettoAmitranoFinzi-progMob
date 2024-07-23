package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey val id: String = "",
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