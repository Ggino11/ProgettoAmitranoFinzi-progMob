package com.amitranofinzi.vimata.data.model


data class User(
    val email: String,
    val name: String,
    val password: String, // Hashed password
    val surname: String,
    val uid: String?, // User ID assigned by Firebase Auth
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
