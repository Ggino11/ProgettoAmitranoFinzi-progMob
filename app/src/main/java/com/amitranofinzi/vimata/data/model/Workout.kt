package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

/**
 * Represents a workout plan created by a trainer for an athlete.
 * This is a Room entity annotated for database table creation.
 * The `workouts` table has foreign key relationships with the `users` table.
 *
 * @property id The unique ID of the workout. And the firebase document ID
 * @property title The title of the workout.
 * @property status The status of the workout.
 * @property trainerID The ID of the trainer who created the workout.
 * @property athleteID The ID of the athlete for whom the workout was created.
 * @property pdfUrl The URL of the PDF containing the workout details.
 */
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