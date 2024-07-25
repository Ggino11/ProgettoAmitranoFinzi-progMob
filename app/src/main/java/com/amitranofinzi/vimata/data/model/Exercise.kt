package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["trainerID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Collection::class,
            parentColumns = ["id"],
            childColumns = ["collectionID"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Exercise(
    @PrimaryKey @DocumentId val id: String = "",
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