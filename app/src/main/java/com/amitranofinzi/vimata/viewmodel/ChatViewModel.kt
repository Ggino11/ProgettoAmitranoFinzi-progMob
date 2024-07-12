package com.amitranofinzi.vimata.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.data.model.Relationship
import com.amitranofinzi.vimata.data.repository.ChatRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
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

    //Create live data for messages
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    //receiverID live data
    private val _receiverId = MutableLiveData<String>()
    val receiverId: LiveData<String> get() = _receiverId

    //fetch single relationship
    fun fetchReceiverId(chatId: String, userType: String) {
        viewModelScope.launch {
            Log.d("ChatViewModel fetch receiver", "sender is ${userType}")
            val fetchedReceiverId = chatRepository.getReceiverId(chatId, userType)
            Log.d("ChatViewModel fetch receiver", "receiver is ${receiverId}")
            _receiverId.setValue(fetchedReceiverId!!)//nullable
        }

    }
    fun fetchRelationships(userID: String, userType: String){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ChatViewModel", "fetching relationships with ${userID} and ${userType}")
            val fetchedRelationships = chatRepository.getRelationships(userID, userType)
            Log.d("ChatViewModel", fetchedRelationships.toString())
            _relationships.postValue(fetchedRelationships)
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
    fun fetchMessages(chatId: String) {
        viewModelScope.launch {
            val fetchedMessages = chatRepository.getMessages(chatId)
            Log.d("MexList", fetchedMessages.toString())
            _messages.value = fetchedMessages

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


    // Function to listen for real-time updates to messages in a specific chat
//    fun listenForMessages(chatId: String, onMessagesChanged: (List<Message>) -> Unit) {
//        chatRepository.listenForMessages(chatId) { messages ->
//            onMessagesChanged(messages)
//            _messages.postValue(messages) // Optionally update LiveData for observing changes
//        }
//    }
    private var chatListener: ListenerRegistration? = null

    fun listenForMessages(chatId: String) {
        chatListener?.remove() //removes existing listener
        Log.d("MexList", chatId)
        chatListener = chatRepository.listenForMessages(chatId) { messages ->
            _messages.value = messages
        }
        Log.d("MexList", chatId)
    }

    override fun onCleared() {
        super.onCleared()
        chatListener?.remove()}
}