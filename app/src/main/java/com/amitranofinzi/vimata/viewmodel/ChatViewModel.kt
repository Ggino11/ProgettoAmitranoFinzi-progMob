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
import kotlinx.coroutines.Dispatchers
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
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    fun fetchRelationship(userID: String, userType: String){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ChatViewModel", "fetching relationships with ${userID} and ${userType}")
            val fetchedRelationships = chatRepository.getRelationships(userID, userType)
            Log.d("ChatViewModel", fetchedRelationships.toString())
            _relationships.postValue(fetchedRelationships)
        }
    }
    // Function to fetch relationships based on user ID and type
    fun fetchChats(relationshipIDs: List<String>) {

        viewModelScope.launch(Dispatchers.IO) {
            val fetchedChats = chatRepository.getChats(relationshipIDs)
            _chats.postValue(fetchedChats)
        }

    }

    // Function to fetch messages based on chat ID
    fun fetchMessages(chatId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val fetchedMessages = chatRepository.getMessages(chatId)
            _messages.value = fetchedMessages
//            _messages.postValue(fetchedMessages)
        }
    }

    // Function to send a message to a specific chat
    fun sendMessage(chatId: String, message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.sendMessage(chatId, message)
        }
    }

    // Function to listen for real-time updates to messages in a specific chat
    fun listenForMessages(chatId: String, onMessagesChanged: (List<Message>) -> Unit) {
        chatRepository.listenForMessages(chatId) { messages ->
            onMessagesChanged(messages)
            _messages.postValue(messages) // Optionally update LiveData for observing changes
        }
    }

}