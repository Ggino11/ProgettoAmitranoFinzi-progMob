package com.amitranofinzi.vimata.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.amitranofinzi.vimata.data.dao.ChatDao
import com.amitranofinzi.vimata.data.dao.MessageDao
import com.amitranofinzi.vimata.data.dao.RelationshipDao
import com.amitranofinzi.vimata.data.dao.UserDao
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.data.model.Relationship
import com.amitranofinzi.vimata.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class ChatRepository(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val relationshipDao: RelationshipDao,
    private val userDao: UserDao,
    private val context: Context
) {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }


    suspend fun getReceiverId(chatId: String, userType: String): String? {
        return if (isNetworkAvailable()) {
            try {
                val snapshotChat = firestore.collection("chats")
                    .document(chatId)
                    .get()
                    .await()

                val relationshipId = snapshotChat.getString("relationshipID")
                val snapshotRelationship = relationshipId?.let {
                    firestore.collection("relationships")
                        .document(it)
                        .get()
                        .await()
                }

                val relationshipObj = snapshotRelationship?.toObject(Relationship::class.java)
                val receiverId = if (userType == "trainer") relationshipObj?.athleteID else relationshipObj?.trainerID

                receiverId
            } catch (e: Exception) {
                Log.e("getReceiverId", "Error fetching receiverId", e)
                null
            }
        } else {
            // Recupera i dati dal database locale
            val chat = chatDao.getChatById(chatId)
            val relationship = chat?.relationshipID?.let { relationshipDao.getRelationshipById(it) }
            if (userType == "trainer") relationship?.athleteID else relationship?.trainerID
        }
    }

    suspend fun getReceiver(userId: String): User? {
        return if (isNetworkAvailable()) {
            try {
                val querySnapshot = firestore.collection("users")
                    .whereEqualTo("uid", userId)
                    .get()
                    .await()

                val user = querySnapshot.documents.firstOrNull()?.toObject(User::class.java)

                user
            } catch (e: Exception) {
                Log.e("getReceiver", "Error fetching user", e)
                null
            }
        } else {
            userDao.getUserById(userId)
        }
    }


    suspend fun getRelationships(userId: String, userType: String): List<Relationship> {
        return if (isNetworkAvailable()) {
            try {
                val query = if (userType == "athlete") {
                    firestore.collection("relationships")
                        .whereEqualTo("athleteID", userId)
                } else {
                    firestore.collection("relationships")
                        .whereEqualTo("trainerID", userId)
                }

                val snapshot = query.get().await()

                val relationships = snapshot.documents.mapNotNull { it.toObject(Relationship::class.java) }

                relationshipDao.insertAll(relationships)

                relationships
            } catch (e: Exception) {
                Log.e("getRelationships", "Error fetching relationships", e)
                emptyList()
            }
        } else {
            relationshipDao.getRelationshipsByUserId(userId, userType)
        }
    }

    //get chats based on relationship id
    suspend fun getChats(relationshipIDs: List<String>): List<Chat> {
        return if (isNetworkAvailable()) {
            try {
                val snapshot = firestore.collection("chats")
                    .whereIn("relationshipID", relationshipIDs)
                    .get()
                    .await()

                val chats = snapshot.documents.mapNotNull { it.toObject(Chat::class.java) }

                // Salva le chat nel database locale
                chatDao.insertAll(chats)

                chats
            } catch (e: Exception) {
                Log.e("getChats", "Error fetching chats", e)
                emptyList()
            }
        } else {
            // Recupera i dati dal database locale
            chatDao.getChatsByRelationshipIDs(relationshipIDs)
        }
    }

    //sends message to a specific chat
    suspend fun sendMessage(chatId: String, message: Message) {
        if (isNetworkAvailable()) {
            try {
                val messageReference = firestore.collection("messages")
                    .add(message)
                    .await()

                val generatedId = messageReference.id
                val document = firestore.collection("messages").document(generatedId)
                document.update("id", generatedId).await()

                firestore.collection("chats").document(chatId)
                    .update("lastMessage", message.text)
                    .await()

                // Salva il messaggio nel database locale
                messageDao.insertMessage(message.copy(id = generatedId))
                chatDao.updateLastMessage(chatId, message.text)
            } catch (e: Exception) {
                Log.e("sendMessage", "Error sending message", e)
            }
        } else {
            // Puoi implementare un meccanismo di salvataggio locale per i messaggi non inviati, se necessario
            Log.e("sendMessage", "No internet connection. Unable to send message.")
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
        return if (isNetworkAvailable()) {
            try {
                val query = if (userType == "athlete") {
                    firestore.collection("relationships")
                        .whereEqualTo("athleteID", userID)
                } else {
                    firestore.collection("relationships")
                        .whereEqualTo("trainerID", userID)
                }

                val snapshot = query.get().await()

                val userIds = snapshot.documents.mapNotNull { document ->
                    val relationship = document.toObject(Relationship::class.java)
                    if (userType == "athlete") relationship?.trainerID else relationship?.athleteID
                }

                val users = userIds.mapNotNull { userId ->
                    val userSnapshot = firestore.collection("users")
                        .document(userId)
                        .get()
                        .await()
                    userSnapshot.toObject(User::class.java)
                }

                // Salva gli utenti nel database locale
                users?.let { userDao.insertAll(it) }

                users
            } catch (e: Exception) {
                Log.e("getReceivers", "Error fetching users", e)
                emptyList()
            }
        } else {
            // Recupera i dati dal database locale
            val relationships = relationshipDao.getRelationshipsByUserId(userID, userType)
            val userIds = relationships.mapNotNull { if (userType == "athlete") it.trainerID else it.athleteID }
            userDao.getUsersByIDs(userIds)
        }
    }

}








