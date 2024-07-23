package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collections")
data class Collection(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val trainerID: String = "",
){  constructor() : this(
    id = "",
    title = "",
    trainerID = ""
)
}