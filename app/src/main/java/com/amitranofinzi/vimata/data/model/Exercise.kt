package com.amitranofinzi.vimata.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

/**
 * Represents an exercise created by a trainer within a collection.
 * This is a Room entity annotated for database table creation.
 * The `exercises` table has foreign key relationships with the `users` and `collections` tables.
 *
 * @property id The unique ID of the exercise. And the firebase document ID
 * @property name The name of the exercise.
 * @property description The description of the exercise.
 * @property videoUrl The URL of the exercise video.
 * @property trainerID The ID of the trainer who created the exercise.
 * @property collectionID The ID of the collection to which this exercise belongs.
 */
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
    ],
    indices = [Index(value = ["trainerID"]), Index(value = ["collectionID"]) ]
)
data class Exercise(
    @PrimaryKey @DocumentId @NonNull val id: String = "",
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