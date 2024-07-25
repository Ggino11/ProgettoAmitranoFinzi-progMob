package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId


@Entity(tableName = "collections",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["trainerID"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Collection(
    @PrimaryKey @DocumentId val id: String = "",
    val title: String = "",
    val trainerID: String = "",
){  constructor() : this(
    id = "",
    title = "",
    trainerID = ""
)
}