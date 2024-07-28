package com.amitranofinzi.vimata.data.dao


import com.amitranofinzi.vimata.data.model.Chat
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class ChatDaoTest {

    private lateinit var chatDao: ChatDao

    @Before
    fun setup() {
        chatDao = mock(ChatDao::class.java)
    }

    @Test
    fun insertChat_retrieveByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val chat = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")
        `when`(chatDao.getWithPrimaryKey("chat1")).thenReturn(chat)

        // Act
        chatDao.insert(chat)
        val retrievedChat = chatDao.getWithPrimaryKey("chat1")

        // Assert
        assertEquals(chat, retrievedChat)
        verify(chatDao).insert(chat)
        verify(chatDao).getWithPrimaryKey("chat1")
    }

    @Test
    fun getWhereEqual_fieldEqualsValue(): Unit = runBlocking {
        // Arrange
        val chat1 = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")
        val chat2 = Chat(chatId = "chat2", relationshipID = "relationship2", lastMessage = "Hi")
        val expectedChats = listOf(chat1)
        `when`(chatDao.getWhereEqual("relationshipID", "relationship1")).thenReturn(expectedChats)

        // Act
        val result = chatDao.getWhereEqual("relationshipID", "relationship1")

        // Assert
        assertEquals(expectedChats, result)
        verify(chatDao).getWhereEqual("relationshipID", "relationship1")
    }

    @Test
    fun getWhereIn_fieldInValues(): Unit = runBlocking {
        // Arrange
        val chat1 = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")
        val chat2 = Chat(chatId = "chat2", relationshipID = "relationship2", lastMessage = "Hi")
        val chat3 = Chat(chatId = "chat3", relationshipID = "relationship3", lastMessage = "Hey")
        val expectedChats = listOf(chat1, chat3)
        `when`(chatDao.getWhereIn("relationshipID", listOf("relationship1", "relationship3"))).thenReturn(expectedChats)

        // Act
        val result = chatDao.getWhereIn("relationshipID", listOf("relationship1", "relationship3"))

        // Assert
        assertEquals(expectedChats, result)
        verify(chatDao).getWhereIn("relationshipID", listOf("relationship1", "relationship3"))
    }

    @Test
    fun updateChat_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val chat = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")
        val updatedChat = chat.copy(lastMessage = "Updated Message")
        `when`(chatDao.getWithPrimaryKey("chat1")).thenReturn(updatedChat)

        // Act
        chatDao.update(updatedChat)
        val retrievedChat = chatDao.getWithPrimaryKey("chat1")

        // Assert
        assertEquals(updatedChat, retrievedChat)
        verify(chatDao).update(updatedChat)
        verify(chatDao).getWithPrimaryKey("chat1")
    }

    @Test
    fun getWithPrimaryKey_noChatFound(): Unit = runBlocking {
        // Arrange
        `when`(chatDao.getWithPrimaryKey("non_existing_chat_id")).thenReturn(null)

        // Act
        val retrievedChat = chatDao.getWithPrimaryKey("non_existing_chat_id")

        // Assert
        assertNull(retrievedChat)
        verify(chatDao).getWithPrimaryKey("non_existing_chat_id")
    }
}
