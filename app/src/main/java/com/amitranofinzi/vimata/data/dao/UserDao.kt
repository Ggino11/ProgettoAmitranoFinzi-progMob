package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amitranofinzi.vimata.data.model.User

@Dao
interface UserDao {

    /**
     * Retrieves a User with a specific primary key.
     *
     * @param uid The primary key of the User to retrieve.
     * @return The User object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM users WHERE uid = :uid")
    suspend fun getUserById(uid: String?): User?

    /**
     * Fetches users based on their IDs.
     * @param userIds: The list of user IDs to fetch.
     * @return A list of users matching the provided IDs.
     */
    @Query("SELECT * FROM users WHERE uid IN (:userIds)")

    suspend fun getUsersByIDs(userIds: List<String>): List<User>

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all User objects.
     */
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    /**
     * Retrieves a user from the database by their email address.
     *
     * @param email The email address of the user to be fetched.
     * @return The User object with the specified email, or null if not found.
     */
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    /**
     * Inserts a User into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param user The User object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    /**
     * Inserts a list of Users into the database.
     *
     * @param users The list of User objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    /**
     * Updates an existing User in the database.
     *
     * @param user The User object to update.
     */
    @Update
    suspend fun update(user: User)
}
