package com.amitranofinzi.vimata.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.data.model.Relationship
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.repository.ChatRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {
    private val chatRepository: ChatRepository = ChatRepository()


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

    //fetch single relationship
    fun fetchReceiverId(chatId: String, userType: String) {
        viewModelScope.launch {
            Log.d("ChatViewModel fetch receiver", "sender is ${userType}")
            val fetchedReceiverId = chatRepository.getReceiverId(chatId, userType)
            Log.d("ChatViewModel fetch receiver", "receiver is ${receiverId}")
            _receiverId.setValue(fetchedReceiverId)//nullable
        }

    }
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

    fun fetchReceivers(userID: String, userType: String){
        viewModelScope.launch {
            Log.d("ChatViewModel", "fetching receivers with ${userID} and ${userType}")
            val fetchedReceivers = chatRepository.getReceivers(userID, userType)
            Log.d("ChatViewModel", fetchedReceivers.toString())
            _receivers.setValue(fetchedReceivers)
        }
    }

    fun fetchRelationships(userID: String, userType: String){
        viewModelScope.launch {
            Log.d("ChatViewModel", "fetching relationships with ${userID} and ${userType}")
            val fetchedRelationships = chatRepository.getRelationships(userID, userType)
            Log.d("ChatViewModel", fetchedRelationships.toString())
            _relationships.setValue(fetchedRelationships)
        }
    }

    // Function to fetch relationships based on user ID and type
    fun fetchChats(relationshipIDs: List<String>) {
        Log.d("RelationshipId", relationshipIDs.isEmpty().toString())
        viewModelScope.launch {
            val fetchedChats = chatRepository.getChats(relationshipIDs)
            _chats.postValue(fetchedChats)
        }

    }

    // Function to fetch messages based on chat ID
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

    // Function to send a message to a specific chat
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

    override fun onCleared() {
        super.onCleared()
        chatListener?.remove()}
}