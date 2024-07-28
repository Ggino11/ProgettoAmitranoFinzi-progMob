package com.amitranofinzi.vimata.data.dao

import com.amitranofinzi.vimata.data.dao.UserDao
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

    // Mock UserDao object
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        // Initialize the UserDao mock
        userDao = mock(UserDao::class.java)
    }

    @Test
    fun insertUser_retrieveByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val user = User(
            uid = "user1",
            email = "john.doe@example.com",
            name = "John",
            password = "hashed_password",
            surname = "Doe",
            userType = "ATHLETE"
        )
        `when`(userDao.getWithPrimaryKey("user1")).thenReturn(user)

        // Act
        userDao.insert(user)
        val retrievedUser = userDao.getWithPrimaryKey("user1")

        // Assert
        assertEquals(user, retrievedUser)
        verify(userDao).insert(user)
        verify(userDao).getWithPrimaryKey("user1")
    }

    @Test
    fun getWhereEqual_fieldEqualsValue(): Unit = runBlocking {
        // Arrange
        val user1 = User(
            uid = "user1",
            email = "john.doe@example.com",
            name = "John",
            password = "hashed_password",
            surname = "Doe",
            userType = "ATHLETE"
        )
        val user2 = User(
            uid = "user2",
            email = "jane.doe@example.com",
            name = "Jane",
            password = "hashed_password",
            surname = "Doe",
            userType = "TRAINER"
        )
        val expectedUsers = listOf(user1)
        `when`(userDao.getWhereEqual("email", "john.doe@example.com")).thenReturn(expectedUsers)

        // Act
        val result = userDao.getWhereEqual("email", "john.doe@example.com")

        // Assert
        assertEquals(expectedUsers, result)
        verify(userDao).getWhereEqual("email", "john.doe@example.com")
    }

    @Test
    fun getWhereIn_fieldInValues(): Unit = runBlocking {
        // Arrange
        val user1 = User(
            uid = "user1",
            email = "john.doe@example.com",
            name = "John",
            password = "hashed_password",
            surname = "Doe",
            userType = "ATHLETE"
        )
        val user2 = User(
            uid = "user2",
            email = "jane.doe@example.com",
            name = "Jane",
            password = "hashed_password",
            surname = "Doe",
            userType = "TRAINER"
        )
        val user3 = User(
            uid = "user3",
            email = "jim.beam@example.com",
            name = "Jim",
            password = "hashed_password",
            surname = "Beam",
            userType = "ATHLETE"
        )
        val expectedUsers = listOf(user1, user3)
        `when`(userDao.getWhereIn("uid", listOf("user1", "user3"))).thenReturn(expectedUsers)

        // Act
        val result = userDao.getWhereIn("uid", listOf("user1", "user3"))

        // Assert
        assertEquals(expectedUsers, result)
        verify(userDao).getWhereIn("uid", listOf("user1", "user3"))
    }

    @Test
    fun updateUser_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val user = User(
            uid = "user1",
            email = "john.doe@example.com",
            name = "John",
            password = "hashed_password",
            surname = "Doe",
            userType = "ATHLETE"
        )
        val updatedUser = user.copy(name = "John Smith")
        `when`(userDao.getWithPrimaryKey("user1")).thenReturn(updatedUser)

        // Act
        userDao.update(updatedUser)
        val retrievedUser = userDao.getWithPrimaryKey("user1")

        // Assert
        assertEquals(updatedUser, retrievedUser)
        verify(userDao).update(updatedUser)
        verify(userDao).getWithPrimaryKey("user1")
    }

    @Test
    fun getWithPrimaryKey_noUserFound(): Unit = runBlocking {
        // Arrange
        `when`(userDao.getWithPrimaryKey("non_existing_user_id")).thenReturn(null)

        // Act
        val retrievedUser = userDao.getWithPrimaryKey("non_existing_user_id")

        // Assert
        assertNull(retrievedUser)
        verify(userDao).getWithPrimaryKey("non_existing_user_id")
    }
}
