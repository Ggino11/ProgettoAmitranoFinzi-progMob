package com.amitranofinzi.vimata.data.dao

import com.amitranofinzi.vimata.data.model.Chat
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class ChatDaoTest {

    private lateinit var chatDao: ChatDao

    @Before
    fun setup() {
        // Mock the ChatDao interface
        chatDao = mock(ChatDao::class.java)
    }

    @Test
    fun getAll_retrieveAllChats(): Unit = runBlocking {
        // Arrange
        val chat1 = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")
        val chat2 = Chat(chatId = "chat2", relationshipID = "relationship2", lastMessage = "Hi")
        val expectedChats = listOf(chat1, chat2)
        `when`(chatDao.getAll()).thenReturn(expectedChats)

        // Act
        val result = chatDao.getAll()

        // Assert
        assertEquals(expectedChats, result)
        verify(chatDao).getAll()
    }

    @Test
    fun getChatById_retrieveSingleChat(): Unit = runBlocking {
        // Arrange
        val chat = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")
        `when`(chatDao.getChatById("chat1")).thenReturn(chat)

        // Act
        val result = chatDao.getChatById("chat1")

        // Assert
        assertEquals(chat, result)
        verify(chatDao).getChatById("chat1")
    }

    @Test
    fun getChatsByRelationshipIDs_retrieveChatsByRelationshipIDs(): Unit = runBlocking {
        // Arrange
        val chat1 = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")
        val chat3 = Chat(chatId = "chat3", relationshipID = "relationship3", lastMessage = "Hey")
        val expectedChats = listOf(chat1, chat3)
        `when`(chatDao.getChatsByRelationshipIDs(listOf("relationship1", "relationship3"))).thenReturn(expectedChats)

        // Act
        val result = chatDao.getChatsByRelationshipIDs(listOf("relationship1", "relationship3"))

        // Assert
        assertEquals(expectedChats, result)
        verify(chatDao).getChatsByRelationshipIDs(listOf("relationship1", "relationship3"))
    }

    @Test
    fun insert_insertsChatCorrectly(): Unit = runBlocking {
        // Arrange
        val chat = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")

        // Act
        chatDao.insert(chat)

        // Assert
        verify(chatDao).insert(chat)
    }

    @Test
    fun insertAll_insertsAllChatsCorrectly(): Unit = runBlocking {
        // Arrange
        val chat1 = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")
        val chat2 = Chat(chatId = "chat2", relationshipID = "relationship2", lastMessage = "Hi")
        val chats = listOf(chat1, chat2)

        // Act
        chatDao.insertAll(chats)

        // Assert
        verify(chatDao).insertAll(chats)
    }

    @Test
    fun update_updatesChatCorrectly(): Unit = runBlocking {
        // Arrange
        val chat = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")

        // Act
        chatDao.update(chat)

        // Assert
        verify(chatDao).update(chat)
    }

    @Test
    fun updateLastMessage_updatesMessageCorrectly(): Unit = runBlocking {
        // Arrange
        val chatId = "chat1"
        val lastMessage = "Updated Message"

        // Act
        chatDao.updateLastMessage(chatId, lastMessage)

        // Assert
        verify(chatDao).updateLastMessage(chatId, lastMessage)
    }

    @Test
    fun deleteByChatId_deletesChatByIdCorrectly(): Unit = runBlocking {
        // Arrange
        val chatId = "chat1"

        // Act
        chatDao.deleteByChatId(chatId)

        // Assert
        verify(chatDao).deleteByChatId(chatId)
    }

    @Test
    fun clearAll_deletesAllChatsCorrectly(): Unit = runBlocking {
        // Act
        chatDao.clearAll()

        // Assert
        verify(chatDao).clearAll()
    }
}
