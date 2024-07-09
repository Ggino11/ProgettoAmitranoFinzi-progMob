package com.amitranofinzi.vimata.data.repository

import android.util.Log
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.data.model.Relationship
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await


class ChatRepository() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    /**
     * Fetches relationships (chats) associated with a specific user ID.
     * takes user id and user type and fetches all relationship for that user
     */
    suspend fun fetchRelationships(userId: String, userType: String): List<Relationship> {
        return try {
            //get snapshot for athlete or trainer based on user type
            val snapshot = if (userType == "athlete") {
                firestore.collection("relationships")
                    .whereEqualTo("athleteID", userId)
                    .get()
                    .await()
            } else {
                firestore.collection("relationships")
                    .whereEqualTo("trainerID", userId)
                    .get()
                    .await()
            }
            snapshot.documents.mapNotNull { it.toObject(Relationship::class.java) }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error fetching relationships", e)
            emptyList()
        }
    }

    //get chats based on relationship id
    suspend fun getChats(relationshipIDs: List<String>): List<Chat> {
        return try {
            val snapshot = firestore.collection("chats")
                .whereIn("relationshipID", relationshipIDs)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Chat::class.java) }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error fetching chats", e)
            emptyList()
        }
    }

    //get messages from a specific chat based on the caht id
    suspend fun getMessages(chatId: String): List<Message> {
        return try {
            val snapshot = firestore.collection("messages")
                .whereEqualTo("chatID", chatId)
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Message::class.java) }

        } catch (e: Exception){
            Log.e("ChatRepo", "Error Fetching messages", e)
            emptyList()
        }
    }

    //sends message to a specific chat
    suspend fun sendMessage(chatId: String, message: Message) {
        try { //add message to messages collection
            firestore.collection("messages")
                .add(message.copy(chatId = chatId))
                .addOnSuccessListener {
                    // Update last message in the chat document just if the message was successfully sent
                    firestore.collection("chats").document(chatId)
                        .update("lastMessage", message.text)
                        .addOnSuccessListener {
                            Log.d("ChatRepo", "Message sent successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("ChatRepo", "Error updating lastMessage", e)
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("ChatRepo", "Error sending message", e)
                }
                .await()
        } catch (e: Exception) {
            Log.e("ChatRepo", "Error sending message")
        }
    }
    /**
     * Listens for real-time updates to messages in a specific chat.
     *
     * @param chatId The ID of the chat for which real-time message updates are to be listened.
     * @param onMessagesChanged Callback function invoked when messages in the chat are updated.
     */
    fun listenForMessages(chatId: String, onMessagesChanged: (List<Message>) -> Unit) {
        firestore.collection("messages")
            .whereEqualTo("chatId", chatId)
            .orderBy("timeStamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ChatRepository", "Error listening for messages", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { it.toObject(Message::class.java) }
                    onMessagesChanged(messages)
                }
            }
    }
}




