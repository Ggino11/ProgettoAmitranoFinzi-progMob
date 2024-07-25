package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity(tableName = "workouts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["trainerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["athleteId"],
            onDelete = ForeignKey.CASCADE
        )]
    )
data class Workout(
    @PrimaryKey @DocumentId val id: String,
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