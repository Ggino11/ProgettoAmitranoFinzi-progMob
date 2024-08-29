package com.amitranofinzi.vimata.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.dao.ChatDao
import com.amitranofinzi.vimata.data.dao.MessageDao
import com.amitranofinzi.vimata.data.dao.RelationshipDao
import com.amitranofinzi.vimata.data.dao.UserDao
import com.amitranofinzi.vimata.data.database.AppDatabase
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.data.model.Relationship
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.repository.ChatRepository
import com.amitranofinzi.vimata.ui.navigation.InitializableViewModel
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing chat-related data and interactions.
 * It handles fetching and updating chats, messages, relationships, and receivers.
 */
class ChatViewModel: ViewModel(), InitializableViewModel {

    lateinit var appDatabase: AppDatabase
    lateinit var context: Context
    private var isInitialized = false
    private lateinit var chatRepository: ChatRepository

    private val relationshipDao: RelationshipDao
        get() = appDatabase.relationshipDao()

    private val userDao: UserDao
        get() = appDatabase.userDao()

    private val messageDao: MessageDao
        get() = appDatabase.messageDao()

    private val chatDao: ChatDao
        get() = appDatabase.chatDao()

    /**
     * Initializes the ViewModel with the provided database and context.
     * This should be called before using the ViewModel.
     *
     * @param appDatabase The application database instance.
     * @param context The application context.
     */
    override fun initialize(appDatabase: AppDatabase, context: Context) {
        if (!isInitialized) {
            this.appDatabase = appDatabase
            this.context = context

            // Inizializza il repository dopo che appDatabase e context sono impostati
            initializeRepositories()

            isInitialized = true
        }
    }

    private fun initializeRepositories() {
        chatRepository = ChatRepository(
            chatDao = chatDao,
            messageDao = messageDao,
            relationshipDao = relationshipDao,
            userDao = userDao,
            context = context
        )
    }



    //Create live data for relationship
    private val _relationships = MutableLiveData<List<Relationship>>()
    val relationships: LiveData<List<Relationship>> get() =_relationships

    //Create live data for chats
    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> get() = _chats

    private val _receiver = MutableLiveData<User?>()
    val receiver: LiveData<User?> = _receiver

    private val _receivers = MutableLiveData<List<User>?>()
    val receivers: LiveData<List<User>?> = _receivers

    //Create live data for messages
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    //receiverID live data
    private val _receiverId = MutableLiveData<String?>()
    val receiverId: MutableLiveData<String?> get() = _receiverId

    /**
     * Fetches the receiver ID for a given chat ID and user type.
     *
     * @param chatId The ID of the chat.
     * @param userType The type of the user.
     */
    fun fetchReceiverId(chatId: String, userType: String) {
        viewModelScope.launch {
            Log.d("ChatViewModel fetch receiver", "sender is ${userType}")
            val fetchedReceiverId = chatRepository.getReceiverId(chatId, userType)
            Log.d("ChatViewModel fetch receiver", "receiver is ${receiverId}")
            _receiverId.setValue(fetchedReceiverId)//nullable
        }

    }

    /**
     * Fetches user details by user ID.
     *
     * @param userID The ID of the user.
     */
    fun fetchReceiverById(userID: String) {
        viewModelScope.launch {
            try {
                Log.d("ChatViewModel", "Fetching user for userID: $userID")
                val fetchedUser = chatRepository.getReceiver(userID)
                Log.d("ChatViewModel", "Fetched user: $fetchedUser")
                _receiver.value = fetchedUser
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error fetching user", e)
                //_receiver.value = null // Assicuriamoci che se c'è un errore, _receiver sia null
            }
        }
    }

    /**
     * Fetches a list of receivers based on user ID and user type.
     *
     * @param userID The ID of the user.
     * @param userType The type of the user.
     */
    fun fetchReceivers(userID: String, userType: String){
        viewModelScope.launch {
            Log.d("ChatViewModel", "fetching receivers with ${userID} and ${userType}")
            val fetchedReceivers = chatRepository.getReceivers(userID, userType)
            Log.d("ChatViewModel", fetchedReceivers.toString())
            _receivers.setValue(fetchedReceivers)
        }
    }

    /**
     * Fetches relationships for a user based on user ID and user type.
     *
     * @param userID The ID of the user.
     * @param userType The type of the user.
     */
    fun fetchRelationships(userID: String, userType: String){
        viewModelScope.launch {
            Log.d("ChatViewModel", "fetching relationships with ${userID} and ${userType}")
            val fetchedRelationships = chatRepository.getRelationships(userID, userType)
            Log.d("ChatViewModel", fetchedRelationships.toString())
            _relationships.setValue(fetchedRelationships)
        }
    }

    /**
     * Fetches chats based on a list of relationship IDs.
     *
     * @param relationshipIDs The list of relationship IDs.
     */
    fun fetchChats(relationshipIDs: List<String>) {
        Log.d("RelationshipId", relationshipIDs.isEmpty().toString())
        viewModelScope.launch {
            val fetchedChats = chatRepository.getChats(relationshipIDs)
            _chats.postValue(fetchedChats)
        }

    }

    /**
     * Listens for messages in a chat and updates the messages state flow.
     *
     * @param chatId The ID of the chat.
     */
    fun listenForMessages(chatId: String) {
        viewModelScope.launch {
            Log.d("ChatViewModel", "Starting to listen for messages for chatId: $chatId")
            chatRepository.getMessagesFlow(chatId).collect { messages ->
                Log.d("ChatViewModel", "Collected ${messages.size} messages for chatId: $chatId")
                messages.forEach { message ->
                    Log.d("ChatViewModel", "Message: ${message.text}, Timestamp: ${message.timeStamp}")
                }
                _messages.value = messages
                Log.d("ChatViewModel", "Updated _messages with ${_messages.value.size} messages for chatId: $chatId")
            }
        }
    }

    /**
     * Sends a message to a specific chat.
     *
     * @param chatId The ID of the chat.
     * @param message The message to be sent.
     */
    fun sendMessage(chatId: String, message: Message) {
        Log.d("ChatViewModel", "Preparing to send message: ${message.text} to chatId: $chatId")
        viewModelScope.launch {
            try {
                chatRepository.sendMessage(chatId, message)
                Log.d("ChatViewModel", "Message sent successfully to chatId: $chatId")
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error sending message to chatId: $chatId", e)
            }
        }
    }

    private var chatListener: ListenerRegistration? = null

    /**
     * Removes the chat listener when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        chatListener?.remove()}
}