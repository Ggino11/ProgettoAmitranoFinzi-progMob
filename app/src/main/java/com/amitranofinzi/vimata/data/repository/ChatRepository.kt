package com.amitranofinzi.vimata.data.repository

import android.util.Log
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.data.model.Relationship
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
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
    suspend fun getRelationships(userId: String, userType: String): List<Relationship> {
        return try {
            Log.d("ChatRepository", "UserID: $userId")
            Log.d("ChatRepository", "UserType: $userType")

            // Scegli la collezione giusta in base al tipo di utente
            val query = if (userType == "athlete") {
                firestore.collection("relationships")
                    .whereEqualTo("athleteID", userId)
            } else {
                firestore.collection("relationships")
                    .whereEqualTo("trainerID", userId)
            }

            val snapshot = query.get().await()

            if (snapshot.isEmpty) {
                Log.d("ChatRepository", "No relationships found for user $userId of type $userType")
                return emptyList()
            }

            val relationships = snapshot.documents.mapNotNull {
                it.toObject(Relationship::class.java)?.apply {
                    Log.d("ChatRepository", "Relationship found: $this")
                }
            }

            Log.d("ChatRepository", "Total relationships found: ${relationships.size}")
            relationships
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error fetching relationships", e)
            emptyList()
        }
    }

    //get chats based on relationship id
    suspend fun getChats(relationshipIDs: List<String>): List<Chat> {
        // Verifica che relationshipIDs non sia vuota
        if (relationshipIDs.isEmpty()) {
            Log.e("ChatRepository", "Error: Empty relationshipIDs list provided")
            return emptyList()
        }

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
        try {
            // Log to debug message being sent
            Log.d("SendChat", "Sending message: ${message.text} to chatId: ${chatId}")

            // Add message to messages collection
            val messageReference = firestore.collection("messages")
                .add(message)
                .await()

            // Log to debug message document reference
            Log.d("SendChat", "Message sent successfully with ID: ${messageReference.id}")

            // Update last message in the chat document
            firestore.collection("chats").document(chatId)
                .update("lastMessage", message.text)
                .await()

            // Log to debug chat document update
            Log.d("SendChat", "Chat document $chatId updated with last message: ${message.text}")
        } catch (e: Exception) {
            // Log error
            Log.e("SendChat", "Error sending message", e)
        }
    }

    /**
     * Listens for real-time updates to messages in a specific chat.
     *
     * @param chatId The ID of the chat for which real-time message updates are to be listened.
     * @param onMessagesChanged Callback function invoked when messages in the chat are updated.
     */
//    fun listenForMessages(chatId: String, onMessagesChanged: (List<Message>) -> Unit) {
//        firestore.collection("messages")
//            .whereEqualTo("chatId", chatId)
//            .orderBy("timeStamp", Query.Direction.ASCENDING)
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    Log.e("ChatRepository", "Error listening for messages", error)
//                    return@addSnapshotListener
//                }
//                if (snapshot != null) {
//                    val messages = snapshot.documents.mapNotNull { it.toObject(Message::class.java) }
//                    onMessagesChanged(messages)
//                }
//            }
//    }
    fun listenForMessages(chatId: String, onMessagesChanged: (List<Message>) -> Unit): ListenerRegistration {
        Log.d("Listener", "Setting up listener for chatId: $chatId")

        return firestore.collection("messages")
            .whereEqualTo("chatId", chatId)
            .orderBy("timestamp") // Assuming the field name is "timestamp" in your Message class
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Listener", "Listen failed for chatId: $chatId", error)
                    onMessagesChanged(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    if (snapshot.isEmpty) {
                        Log.d("Listener", "No messages found for chatId: $chatId")
                        onMessagesChanged(emptyList())
                    } else {
                        val messages = snapshot.documents.mapNotNull {
                            it.toObject(Message::class.java)
                        }
                        Log.d("Listener", "Received ${messages.size} messages for chatId: $chatId")
                        onMessagesChanged(messages)
                    }
                } else {
                    Log.d("Listener", "Snapshot is null for chatId: $chatId")
                    onMessagesChanged(emptyList())
                }
            }
    }

}






