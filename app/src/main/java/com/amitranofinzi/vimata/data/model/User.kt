package com.amitranofinzi.vimata.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 * Represents a user in the application.
 * This is a Room entity annotated for database table creation.
 *
 * @property uid The unique ID of the user. And the firebase document ID
 * @property name The name of the user.
 * @property email The email address of the user.
 * @property surname The last name of the user
 * @property userType if the user is an athlete or a trainer
 * @property password the password of the user stored for testing purposes
 */

@Entity(tableName="users")
data class User(
    @PrimaryKey @NonNull val uid: String, // User ID assigned by Firebase Auth
    val email: String,
    val name: String,
    val password: String, // Hashed password for now stored in db for teting purposes, need to be deleted
    val surname: String,
    val userType: String, //maybe define an enum for UserType (ATHLETE, TRAINER)

) {
    // Required for Firestore deserialization
    constructor() : this(
        email = "",
        name = "",
        password = "",
        surname = "",
        uid = "",
        userType = ""
    )
}
