package com.amitranofinzi.vimata.data.dao


import com.amitranofinzi.vimata.data.model.Message
import com.google.firebase.Timestamp
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`


class MessageDaoTest {

    // Mock MessageDao object
    private lateinit var messageDao: MessageDao

    @Before
    fun setup() {
        // Initialize the MessageDao mock
        messageDao = mock(MessageDao::class.java)
    }

    @Test
    fun insertAndRetrieveMessage(): Unit = runBlocking {
        // Arrange
        val message = Message(
            id = "msg1",
            chatId = "chat1",
            senderId = "user1",
            text = "Hello, World!",
            timeStamp = Timestamp.now(),
            receiverId = "user2"
        )
        `when`(messageDao.getMessagesByChatId("chat1")).thenReturn(flowOf(listOf(message)))

        // Act
        messageDao.insertMessage(message)
        val retrievedMessages = messageDao.getMessagesByChatId("chat1").first()

        // Assert
        assertEquals(1, retrievedMessages.size)
        assertEquals(message.text, retrievedMessages[0].text)
        verify(messageDao).insertMessage(message)
        verify(messageDao).getMessagesByChatId("chat1")
    }

    @Test
    fun updateMessage_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val message = Message(
            id = "msg1",
            chatId = "chat1",
            senderId = "user1",
            text = "Hello, World!",
            timeStamp = Timestamp.now(),
            receiverId = "user2"
        )
        val updatedMessage = message.copy(text = "Updated Message")
        `when`(messageDao.getMessagesByChatId("chat1")).thenReturn(flowOf(listOf(updatedMessage)))

        // Act
        messageDao.insertMessage(updatedMessage)
        val retrievedMessages = messageDao.getMessagesByChatId("chat1").first()

        // Assert
        assertEquals(1, retrievedMessages.size)
        assertEquals(updatedMessage.text, retrievedMessages[0].text)
        verify(messageDao).insertMessage(updatedMessage)
        verify(messageDao).getMessagesByChatId("chat1")
    }

    @Test
    fun getMessagesByChatId_noMessagesFound(): Unit = runBlocking {
        // Arrange
        `when`(messageDao.getMessagesByChatId("non_existing_chat_id")).thenReturn(flowOf(emptyList()))

        // Act
        val retrievedMessages = messageDao.getMessagesByChatId("non_existing_chat_id").first()

        // Assert
        assertTrue(retrievedMessages.isEmpty())
        verify(messageDao).getMessagesByChatId("non_existing_chat_id")
    }
}
