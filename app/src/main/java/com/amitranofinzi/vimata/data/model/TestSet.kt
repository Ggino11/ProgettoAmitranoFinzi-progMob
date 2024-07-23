package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "testSets")
data class TestSet (
    @PrimaryKey val id: String = "",
    val title: String = "",
    val trainerID: String = "",
    val athleteID: String = "",
)
{    constructor() : this(
    id = "",
    title = "",
    trainerID = "",
    athleteID = "",
    )
}