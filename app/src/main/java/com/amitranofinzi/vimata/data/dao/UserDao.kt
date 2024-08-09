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
     * Retrieves a list of User where the value of a specific field equals a given value.
     *
     * @param field The name of the field to be compared.
     * @param value The value to be compared with the specified field.
     * @return A list of User objects that meet the equality condition.
     */
    @Query("SELECT * FROM users WHERE :field = :value")
    suspend fun getWhereEqual(field: String, value: String): List<User>

    /**
     * Retrieves a list of User where the value of a specific field is in a list of values.
     *
     * @param field The name of the field to be compared.
     * @param values The list of values to be compared with the specified field.
     * @return A list of User objects that meet the inclusion condition.
     */
    @Query("SELECT * FROM users WHERE :field IN (:values)")
    suspend fun getWhereIn(field: String, values: List<String>): List<User>

    /**
     * Retrieves a User with a specific primary key.
     *
     * @param uid The primary key of the User to retrieve.
     * @return The User object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM users WHERE uid = :uid")
    suspend fun getWithPrimaryKey(uid: String?): User?

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
