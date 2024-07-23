package com.amitranofinzi.vimata.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="users")
data class User(
    @PrimaryKey val uid: String?, // User ID assigned by Firebase Auth
    val email: String,
    val name: String,
    val password: String, // Hashed password for now stored in db for teting purposes, need to be deleted
    val surname: String,
    val userType: String, //maybe define an enum for UserType (ATHLETE, TRAINER)
    //val age: Int,
    //val profilePictureUrl: String? = null, // nullable for optional profile picture
    //val bio: String? = null // Optional user bio
) {
    // Required for Firestore deserialization
    constructor() : this(
        email = "",
        name = "",
        password = "",
        surname = "",
        uid = null,
        userType = ""
    )
}
