package com.amitranofinzi.vimata.data.model


data class User(
    val uid: String?, // User ID assigned by Firebase Auth
    val name: String,
    val surname: String,
    val email: String,
    val password: String, // Hashed password (IMPORTANT: never store plain text passwords!)
    val userType: String, // You can define an enum for UserType (ATHLETE, TRAINER)
    //val age: Int,
    //val pictureUrl: String? = null, // nullable for optional profile picture
    //val bio: String? = null // Optional user bio
) {
    // Converts User object to a Map, useful for Firebase Firestore
//    fun toMap(): Map<String, Any?> {
//        return mapOf(
//            "uid" to uid,
//            "name" to name,
//            "lastName" to lastName,
//            "email" to email,
//            "password" to password,
//            "userType" to userType.name,
//            "age" to age,
//            "pictureUrl" to pictureUrl,
//            "bio" to bio
//        )
//    }
//
//    companion object {
//        // Converts a Map to a User object, useful for Firebase Firestore
//        fun fromMap(map: Map<String, Any?>): User {
//            return User(
//                uid = map["uid"] as String,
//                name = map["name"] as String,
//                lastName = map["lastName"] as String,
//                email = map["email"] as String,
//                password = map["password"] as String,
//                userType = UserType.valueOf(map["userType"] as String),
//                age = map["age"] as Int,
//                pictureUrl = map["pictureUrl"] as String?,
//                bio = map["bio"] as String?
//            )
//        }
//    }
}

// Define user type enum
enum class UserType {
    ATHLETE,
    TRAINER
}
