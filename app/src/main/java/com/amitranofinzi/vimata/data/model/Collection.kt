package com.amitranofinzi.vimata.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents a collection of exercises created by a trainer.
 * This is a Room entity annotated for database table creation.
 * The `collections` table has a foreign key relationship with the `users` table.
 *
 * @property id The unique ID of the collection.
 * @property title The title of the collection.
 * @property trainerID The ID of the trainer who created the collection.
 */

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
    @PrimaryKey @NonNull
    val id: String = "",
    val title: String = "",
    val trainerID: String = "",
){  constructor() : this(
    id = "",
    title = "",
    trainerID = ""
)
}