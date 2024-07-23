package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "relationships")
data class Relationship(
    @PrimaryKey val id: String,
    val athleteID: String,
    val trainerID: String?
) {
    constructor() : this (
        id = "",
        athleteID = "",
        trainerID = ""
    )
}