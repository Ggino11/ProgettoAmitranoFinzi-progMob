package com.amitranofinzi.vimata.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.amitranofinzi.vimata.data.database.AppDatabase
import com.amitranofinzi.vimata.data.model.Chat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var chatDao: ChatDao

    @Before
    fun setup() {
        // Initialize the in-memory database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        chatDao = database.chatDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertChat_retrieveByPrimaryKey() = runBlocking {
        // Arrange
        val chat = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")

        // Act
        chatDao.insert(chat)
        val retrievedChat = chatDao.getWithPrimaryKey("chat1")

        // Assert
        assertEquals(chat, retrievedChat)
    }

    @Test
    fun getWhereEqual_fieldEqualsValue() = runBlocking {
        // Arrange
        val chat1 = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")
        val chat2 = Chat(chatId = "chat2", relationshipID = "relationship2", lastMessage = "Hi")
        chatDao.insert(chat1)
        chatDao.insert(chat2)

        // Act
        val result = chatDao.getWhereEqual("relationshipID", "relationship1")

        // Assert
        assertEquals(listOf(chat1), result)
    }

    @Test
    fun getWhereIn_fieldInValues() = runBlocking {
        // Arrange
        val chat1 = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")
        val chat2 = Chat(chatId = "chat2", relationshipID = "relationship2", lastMessage = "Hi")
        val chat3 = Chat(chatId = "chat3", relationshipID = "relationship3", lastMessage = "Hey")
        chatDao.insert(chat1)
        chatDao.insert(chat2)
        chatDao.insert(chat3)

        // Act
        val result = chatDao.getWhereIn("relationshipID", listOf("relationship1", "relationship3"))

        // Assert
        assertEquals(listOf(chat1, chat3), result)
    }

    @Test
    fun updateChat_checkUpdatedValues() = runBlocking {
        // Arrange
        val chat = Chat(chatId = "chat1", relationshipID = "relationship1", lastMessage = "Hello")
        chatDao.insert(chat)

        // Act
        val updatedChat = chat.copy(lastMessage = "Updated Message")
        chatDao.update(updatedChat)
        val retrievedChat = chatDao.getWithPrimaryKey("chat1")

        // Assert
        assertEquals(updatedChat, retrievedChat)
    }

    @Test
    fun getWithPrimaryKey_noChatFound() = runBlocking {
        // Act
        val retrievedChat = chatDao.getWithPrimaryKey("non_existing_chat_id")

        // Assert
        assertNull(retrievedChat)
    }
}
