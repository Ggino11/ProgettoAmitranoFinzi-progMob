package com.amitranofinzi.vimata.data.dao

import com.amitranofinzi.vimata.data.model.User
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class UserDaoTest {

    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        // Mock the UserDao interface
        userDao = mock(UserDao::class.java)
    }

    @Test
    fun getUserById_retrieveUserByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val user = User(uid = "user1", email = "user1@example.com", name = "John", surname = "Doe", password = "password", userType = "ATHLETE")
        `when`(userDao.getUserById("user1")).thenReturn(user)

        // Act
        val result = userDao.getUserById("user1")

        // Assert
        assertEquals(user, result)
        verify(userDao).getUserById("user1")
    }

    @Test
    fun getUserById_noUserFound(): Unit = runBlocking {
        // Arrange
        `when`(userDao.getUserById("non_existing_user")).thenReturn(null)

        // Act
        val result = userDao.getUserById("non_existing_user")

        // Assert
        assertNull(result)
        verify(userDao).getUserById("non_existing_user")
    }

    @Test
    fun getUsersByIDs_retrieveUsersByListOfIDs(): Unit = runBlocking {
        // Arrange
        val users = listOf(
            User(uid = "user1", email = "user1@example.com", name = "John", surname = "Doe", password = "password", userType = "ATHLETE"),
            User(uid = "user2", email = "user2@example.com", name = "Jane", surname = "Smith", password = "password", userType = "TRAINER")
        )
        `when`(userDao.getUsersByIDs(listOf("user1", "user2"))).thenReturn(users)

        // Act
        val result = userDao.getUsersByIDs(listOf("user1", "user2"))

        // Assert
        assertEquals(users, result)
        verify(userDao).getUsersByIDs(listOf("user1", "user2"))
    }

    @Test
    fun getAllUsers_retrieveAllUsers(): Unit = runBlocking {
        // Arrange
        val users = listOf(
            User(uid = "user1", email = "user1@example.com", name = "John", surname = "Doe", password = "password", userType = "ATHLETE"),
            User(uid = "user2", email = "user2@example.com", name = "Jane", surname = "Smith", password = "password", userType = "TRAINER")
        )
        `when`(userDao.getAllUsers()).thenReturn(users)

        // Act
        val result = userDao.getAllUsers()

        // Assert
        assertEquals(users, result)
        verify(userDao).getAllUsers()
    }

    @Test
    fun getUserByEmail_retrieveUserByEmail(): Unit = runBlocking {
        // Arrange
        val user = User(uid = "user1", email = "user1@example.com", name = "John", surname = "Doe", password = "password", userType = "ATHLETE")
        `when`(userDao.getUserByEmail("user1@example.com")).thenReturn(user)

        // Act
        val result = userDao.getUserByEmail("user1@example.com")

        // Assert
        assertEquals(user, result)
        verify(userDao).getUserByEmail("user1@example.com")
    }

    @Test
    fun insertUserAndRetrieve(): Unit = runBlocking {
        // Arrange
        val user = User(uid = "user1", email = "user1@example.com", name = "John", surname = "Doe", password = "password", userType = "ATHLETE")

        // Act
        userDao.insert(user)
        `when`(userDao.getUserById("user1")).thenReturn(user)
        val retrievedUser = userDao.getUserById("user1")

        // Assert
        assertEquals(user, retrievedUser)
        verify(userDao).insert(user)
        verify(userDao).getUserById("user1")
    }

    @Test
    fun insertAllUsersAndRetrieve(): Unit = runBlocking {
        // Arrange
        val users = listOf(
            User(uid = "user1", email = "user1@example.com", name = "John", surname = "Doe", password = "password", userType = "ATHLETE"),
            User(uid = "user2", email = "user2@example.com", name = "Jane", surname = "Smith", password = "password", userType = "TRAINER")
        )

        // Act
        userDao.insertAll(users)
        `when`(userDao.getUsersByIDs(listOf("user1", "user2"))).thenReturn(users)
        val result = userDao.getUsersByIDs(listOf("user1", "user2"))

        // Assert
        assertEquals(users, result)
        verify(userDao).insertAll(users)
        verify(userDao).getUsersByIDs(listOf("user1", "user2"))
    }

    @Test
    fun updateUser_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val user = User(uid = "user1", email = "user1@example.com", name = "John", surname = "Doe", password = "password", userType = "ATHLETE")
        val updatedUser = user.copy(name = "John Updated")
        `when`(userDao.getUserById("user1")).thenReturn(updatedUser)

        // Act
        userDao.update(updatedUser)
        val retrievedUser = userDao.getUserById("user1")

        // Assert
        assertEquals(updatedUser, retrievedUser)
        verify(userDao).update(updatedUser)
        verify(userDao).getUserById("user1")
    }
}
