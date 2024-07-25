package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity(tableName = "testSets",
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
        )],
    )
data class TestSet (
    @PrimaryKey @DocumentId val id: String = "",
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