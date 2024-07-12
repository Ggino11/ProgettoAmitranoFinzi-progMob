package com.amitranofinzi.vimata.data.repository

//import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.amitranofinzi.vimata.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    //funciton to check if email already exists
    suspend fun checkEmailExists(email: String): Boolean {
        //check if email is already registered in firestore
        val usersCollection = firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()
        return !usersCollection.isEmpty //if is not empty return true

  }
    suspend fun register(email: String, password: String, userType: String, name: String, surname: String): Result<Unit> {
        return try {
            val authResult = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user
            val userId = user?.uid ?: throw IllegalStateException("User ID is null")

            val newUser = User(
                uid = userId,
                name = name,
                surname = surname,
                email = email,
                userType = userType,
                password = password
            )

            firestore.collection("users").document(userId).set(newUser).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }


    // --------------------- GET FUNCTIONS-------------------------//
    suspend fun getUser(userId: String): User? {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error fetching user", e)
            null
        }
    }

    suspend fun getUserType(userId: String): Result<String> {
        return try {
            val document = firestore.collection("users").document(userId).get().await()
            val userType = document.getString("userType")
            if (userType != null) {
                Result.success(userType)
            } else {
                Result.failure(Exception("UserType not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
