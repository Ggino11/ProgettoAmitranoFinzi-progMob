package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey val id: String,
    val title: String,
    val status: String,
    val trainerID: String?,
    val athleteID: String?,
    val pdfUrl: String
)
{    constructor() : this(
    id = "",
    title = "",
    status = "",
    trainerID = null,
    athleteID = null,
    pdfUrl = ""
)

}