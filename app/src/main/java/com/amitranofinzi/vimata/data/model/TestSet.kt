package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId


/**
 * Represents a set of tests created by a trainer for an athlete.
 * This is a Room entity annotated for database table creation.
 * The `testSets` table has foreign key relationships with the `users` table.
 *
 * @property id The unique ID of the test set. And the firebase document ID
 * @property title The title of the test set.
 * @property trainerID The ID of the trainer who created the test set.
 * @property athleteID The ID of the athlete for whom the test set was created.
 */

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