package com.amitranofinzi.vimata.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amitranofinzi.vimata.data.model.User

@Dao
interface UserDao {
    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The User object if found, null otherwise.
     */
    @Query("SELECT * FROM users WHERE uid = :userId LIMIT 1")
    suspend fun getUserById(userId: String): User?

    /**
     * Inserts a user into the database.
     *
     * @param user The user to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    /**
     * Retrieves a list of users by their IDs.
     *
     * @param userIds The list of user IDs to retrieve.
     * @return A list of User objects.
     */
    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    suspend fun getUsersByIds(userIds: List<String>): List<User>
}