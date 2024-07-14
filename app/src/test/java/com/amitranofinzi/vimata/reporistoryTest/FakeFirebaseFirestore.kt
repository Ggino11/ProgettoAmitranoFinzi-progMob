package com.example.firestoretest

import com.amitranofinzi.vimata.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class DatabaseInteractor(private val db: FirebaseFirestore) {
    private var userData: DocumentReference? = null

    // Metodo per aggiungere un utente a Firestore
    fun addUser(user: User) {
        userData = user.uid?.let { db.collection("users").document(it) }
        userData!!.set(user)
    }

    // Metodo per ottenere un utente da Firestore
    fun getUser(uid: String?): Task<DocumentSnapshot> {
        userData = db.collection("users").document(uid!!)
        return userData!!.get()
    }

    // Metodo per controllare se un'email esiste
    fun checkEmailExists(email: String?): Task<QuerySnapshot> {
        return db.collection("users").whereEqualTo("email", email).get()
    }
}
