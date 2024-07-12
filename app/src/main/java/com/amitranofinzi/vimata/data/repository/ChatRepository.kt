package com.amitranofinzi.vimata.data.repository

import android.util.Log
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.data.model.Relationship
import com.amitranofinzi.vimata.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await


class ChatRepository() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

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
            val snapshotRelationship = firestore.collection("relationships")
                .document(relationshipId!!)
                .get()
                .await()
            Log.d("getReceiverId", "Fetched relationship document for relationshipId: $relationshipId")

            // Convert the document to Relationship object
            val relationshipObj = snapshotRelationship.toObject(Relationship::class.java)!!
            Log.d("getReceiverId", "Converted relationship document to Relationship object: $relationshipObj")

            // Determine and return the receiverId based on userType
            val receiverId = if (userType == "trainer") relationshipObj.athleteID else relationshipObj.trainerID
            Log.d("getReceiverId", "Determined receiverId: $receiverId for chatId: $chatId and userType: $userType")

            return receiverId
        } catch (e: Exception) {
            // Log any error that occurs during the function execution
            Log.e("getReceiverId", "Error fetching receiverId for chatId: $chatId", e)
            return null
        }
    }



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
            Log.d("ChatRepo", "Starting to fetch messages for chatId: $chatId")

            val snapshot = firestore.collection("messages")
                .whereEqualTo("chatId", chatId)
                .get().await()

            Log.d("ChatRepo", "Constructed query: $snapshot")

            val messages = snapshot.documents.mapNotNull {
                val message = it.toObject(Message::class.java)
                if (message != null) {
                    Log.d("ChatRepo", "Fetched message: ${message.text} with timestamp: ${message.timeStamp}")
                } else {
                    Log.w("ChatRepo", "Failed to convert document to Message object")
                }
                message
            }
            //return list of messages
            Log.d("ChatRepo", "Successfully fetched ${messages.size} messages for chatId: $chatId")
            messages

        } catch (e: Exception) {
            Log.e("ChatRepo", "Error fetching messages for chatId: $chatId", e)
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

    /**
     * Listens for real-time updates to messages in a specific chat.
     *
     * @param chatId The ID of the chat for which real-time message updates are to be listened.
     * @param onMessagesChanged Callback function invoked when messages in the chat are updated.

    fun listenForMessages(chatId: String, onMessagesChanged: (List<Message>) -> Unit): ListenerRegistration {
        Log.d("Listener", "Setting up listener for chatId: $chatId")

        return firestore.collection("messages")
            .whereEqualTo("chatId", chatId)
            .orderBy("timeStamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Listener", "Listen failed for chatId: $chatId", error)
                    onMessagesChanged(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    if (snapshot.isEmpty) {
                        Log.d("ListenerForChat", "No messages found for chatId: $chatId")
                        onMessagesChanged(emptyList())
                    } else {
                        val messages = snapshot.documents.mapNotNull {
                            it.toObject(Message::class.java)
                        }
                        Log.d("ListenerForChat", "Received ${messages.size} messages for chatId: $chatId")
                        onMessagesChanged(messages)
                    }
                } else {
                    Log.d("ListenerForChat", "Snapshot is null for chatId: $chatId")
                    onMessagesChanged(emptyList())
                }
            }
    }*/
    fun listenForMessages(chatId: String, onMessagesChanged: (List<Message>) -> Unit): ListenerRegistration {
        Log.d("Listener", "Setting up listener for chatId: $chatId")

        // Setup Firestore query to listen for messages in the specified chatId
        return firestore.collection("messages")
            .whereEqualTo("chatId", chatId)
            //.orderBy("timeStamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error if listener fails
                    Log.e("Listener", "Listen failed for chatId: $chatId", error)
                    onMessagesChanged(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    // Check if snapshot is empty
                    if (snapshot.isEmpty) {
                        Log.d("ListenerForChat", "No messages found for chatId: $chatId")
                        onMessagesChanged(emptyList())
                    } else {
                        // Extract messages from snapshot documents
                        val messages = snapshot.documents.mapNotNull { document ->
                            document.toObject(Message::class.java)
                        }

                        // Log and notify UI of received messages
                        Log.d("ListenerForChat", "Received ${messages.size} messages for chatId: $chatId")
                        onMessagesChanged(messages.toList()) // Use toList() to trigger UI update

                        // Optionally, create a copy of messages to ensure immutability
                        val messagesCopy = messages.toList()
                        // Use messagesCopy in further operations to maintain original data integrity
                    }
                } else {
                    // Handle null snapshot scenario
                    Log.d("ListenerForChat", "Snapshot is null for chatId: $chatId")
                    onMessagesChanged(emptyList())
                }
            }
    }

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
//    fun listenForMessages(chatId: String): Flow<List<Message>> = callbackFlow {
//        Log.d("Listener", "Setting up listener for chatId: $chatId")
//
//        val registration = firestore.collection("messages")
//            .whereEqualTo("chatId", chatId)
//            .orderBy("timestamp") // Assicurati che il campo sia corretto
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    Log.e("Listener", "Listen failed for chatId: $chatId", error)
//                    trySend(emptyList())
//                    return@addSnapshotListener
//                }
//
//                val messages = snapshot?.documents?.mapNotNull { doc ->
//                    doc.toObject(Message::class.java).also { message ->
//                        if (message == null) {
//                            Log.w("ListenerForChat", "Failed to convert document to Message object")
//                        }
//                    }
//                }.orEmpty()
//
//                Log.d("ListenerForChat", "Received ${messages.size} messages for chatId: $chatId")
//                trySend(messages)
//            }
//        awaitClose { registration.remove() }
//    }








