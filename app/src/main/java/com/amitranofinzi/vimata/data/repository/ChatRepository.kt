package com.amitranofinzi.vimata.data.repository

import android.util.Log
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.data.model.Relationship
import com.amitranofinzi.vimata.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class ChatRepository() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Fetches relationships (chats) associated with a specific user ID.
     * takes user id and user type and fetches all relationship for that user
     */

    suspend fun getReceiverId(chatId: String, userType: String): String? {
        try {
            // Log the start of the function
            Log.d("getReceiverId", "Starting to fetch receiverId for chatId: $chatId and userType: $userType")

            // Fetch the chat document
            val snapshotChat = firestore.collection("chats")
                .document(chatId)
                .get()
                .await()
            Log.d("getReceiverId", "Fetched chat document for chatId: $chatId")

            // Get the relationshipId from the chat document
            val relationshipId = snapshotChat.getString("relationshipID")
            Log.d("getReceiverId", "Extracted relationshipId: $relationshipId for chatId: $chatId")

            // Fetch the relationship document using the relationshipId
            val snapshotRelationship = relationshipId?.let {
                firestore.collection("relationships")
                    .document(it)
                    .get()
                    .await()
            }
            Log.d("getReceiverId", "Fetched relationship document for relationshipId: $relationshipId")

            // Convert the document to Relationship object
            val relationshipObj = snapshotRelationship?.toObject(Relationship::class.java)
            Log.d("getReceiverId", "Converted relationship document to Relationship object: $relationshipObj")

            // Determine and return the receiverId based on userType
            val receiverId = if (userType == "trainer") relationshipObj?.athleteID else relationshipObj?.trainerID
            Log.d("getReceiverId", "Determined receiverId: $receiverId for chatId: $chatId and userType: $userType")

            return receiverId
        } catch (e: Exception) {
            // Log any error that occurs during the function execution
            Log.e("getReceiverId", "Error fetching receiverId for chatId: $chatId", e)
            return null
        }
    }

    suspend fun getReceiver(userId: String): User? {
        return try {
            Log.d("ChatRepository", "Fetching user with ID: $userId")

            val querySnapshot = firestore.collection("users")
                .whereEqualTo("uid", userId)
                .get()
                .await()


            if (!querySnapshot.isEmpty) {
                val documentSnapshot = querySnapshot.documents.first()
                val user = documentSnapshot.toObject(User::class.java)
                Log.d("ChatRepository", "User found: $user")
                user
            } else {
                Log.d("ChatRepository", "No user found with ID: $userId")
                null
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error fetching user", e)
            null
        }
    }
    /** FETCH ALL REALATIONSHIP
     * @param userId: to compare with ids in relationships
     * @param userType: to get either trainer id or athlete
     * return
     * */
    suspend fun getRelationships(userId: String, userType: String): List<Relationship> {
        return try {
            Log.d("ChatRepository", "UserID: $userId")
            Log.d("ChatRepository", "UserType: $userType")

            // query firestore to get all relatioships where id is equal to athlete or trainer id
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

    //sends message to a specific chat
    suspend fun sendMessage(chatId: String, message: Message) {
        try {
            // Log to debug message being sent
            Log.d("SendChat", "Sending message: ${message.text} to chatId: ${chatId}")

            // Add message to messages collection
            val messageReference = firestore.collection("messages")
                .add(message)
                .await()
            //copy firestore id into id field of document message
            val generatedId = messageReference.id
            val document = firestore.collection("messages").document(generatedId)
            document.update("id", generatedId).await()

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

    suspend fun getChat(chatId: String): Chat? {
        return try {
            val documentSnapshot = firestore.collection("chats")
                .document(chatId)
                .get()
                .await()

            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(Chat::class.java)
            } else {
                Log.d("ChatRepository", "No chat found with ID: $chatId")
                null
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error fetching chat", e)
            null
        }
    }

    suspend fun getRelationship(relationshipId: String): Relationship? {
        return try {
            val documentSnapshot = firestore.collection("relationships")
                .document(relationshipId)
                .get()
                .await()

            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(Relationship::class.java)
            } else {
                Log.d("ChatRepository", "No relationship found with ID: $relationshipId")
                null
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error fetching relationship", e)
            null
        }
    }

    suspend fun getUser(userId: String): User? {
        return try {
            val documentSnapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(User::class.java)
            } else {
                Log.d("ChatRepository", "No user found with ID: $userId")
                null
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error fetching user", e)
            null
        }
    }

    // creates a listner to obtain real time messages in the chat
    fun getMessagesFlow(chatId: String): Flow<List<Message>> = callbackFlow {
        Log.d("ChatRepository", "Starting getMessagesFlow for chatId: $chatId")
        // Register a Firestore snapshot listener to listen for changes in the "messages" collection
        val listenerRegistration = firestore.collection("messages")
            .whereEqualTo("chatId", chatId)
            .orderBy("timeStamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("ChatRepository", "Error in snapshot listener", e)
                    close(e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        val messages = snapshot.toObjects(Message::class.java)
                        Log.d("ChatRepository", "Fetched ${messages.size} messages for chatId: $chatId")
                        val sendResult = trySend(messages).isSuccess
                        Log.d("ChatRepository", "Sending messages success: $sendResult")
                    } else {
                        Log.d("ChatRepository", "No messages found for chatId: $chatId")
                    }
                } else {
                    Log.d("ChatRepository", "Snapshot is null for chatId: $chatId")
                }
            }

        Log.d("ChatRepository", "Listener registered for chatId: $chatId")
        // Await the closing of the flow and remove the listener when the flow is closed
        awaitClose {
            listenerRegistration.remove()
            Log.d("ChatRepository", "Listener removed for chatId: $chatId")
        }
    }

    //get users that are receiving the messages
    suspend fun getReceivers(userID: String, userType: String): List<User>? {
            return try {
                Log.d("ChatRepository", "UserID: $userID")
                Log.d("ChatRepository", "UserType: $userType")

                val query = if (userType == "athlete") {
                    firestore.collection("relationships")
                        .whereEqualTo("athleteID", userID)
                } else {
                    firestore.collection("relationships")
                        .whereEqualTo("trainerID", userID)
                }

                val snapshot = query.get().await()

                if (snapshot.isEmpty) {
                    Log.d("ChatRepository", "No relationships found for user $userID of type $userType")
                    return emptyList()
                }

                val userIds = snapshot.documents.mapNotNull { document ->
                    val relationship = document.toObject(Relationship::class.java)
                    relationship?.let {
                        if (userType == "athlete") it.trainerID else it.athleteID
                    }
                }

                val users = userIds.mapNotNull { userId ->
                    val userSnapshot = firestore.collection("users")
                        .document(userId)
                        .get()
                        .await()
                    userSnapshot.toObject(User::class.java)?.apply {
                        Log.d("ChatRepository", "User found: $this")
                    }
                }

                Log.d("ChatRepository", "Total users found: ${users.size}")
                users
            } catch (e: Exception) {
                Log.e("ChatRepository", "Error fetching users", e)
                emptyList()
            }
        }
    }







