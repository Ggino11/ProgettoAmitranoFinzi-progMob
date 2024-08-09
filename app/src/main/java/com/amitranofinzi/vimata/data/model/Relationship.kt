package com.amitranofinzi.vimata.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName

/**
 * Represents a relationship between a trainer and an athlete in the application.
 * This is a Room entity annotated for database table creation.
 * The `relationships` table has two foreign keys with the `users` table.
 *
 * @property relationshipID The unique ID of the relationship. And the firebase document ID
 * @property trainerID The ID of the trainer in the relationship.
 * @property athleteID The ID of the athlete in the relationship.
 */

@Entity(tableName = "relationships",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["athleteID"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["trainerID"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index(value = ["athleteID"]), Index(value = ["trainerID"])]
)
data class Relationship(
    @PrimaryKey @PropertyName("id") @NonNull var id: String,
    @PropertyName("athleteID") var athleteID: String,
    @PropertyName("trainerID") var trainerID: String?
) {
    constructor() : this (
        id = "",
        athleteID = "",
        trainerID = ""
    )
}