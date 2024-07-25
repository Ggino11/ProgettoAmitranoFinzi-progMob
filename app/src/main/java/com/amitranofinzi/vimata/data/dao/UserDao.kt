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
    fun getWhereEqual(field: String, value: String): List<User>

    /**
     * Retrieves a list of User where the value of a specific field is in a list of values.
     *
     * @param field The name of the field to be compared.
     * @param values The list of values to be compared with the specified field.
     * @return A list of User objects that meet the inclusion condition.
     */
    @Query("SELECT * FROM users WHERE :field IN (:values)")
    fun getWhereIn(field: String, values: List<String>): List<User>

    /**
     * Retrieves a User with a specific primary key.
     *
     * @param uid The primary key of the User to retrieve.
     * @return The User object with the specified primary key, or null if not found.
     */
    @Query("SELECT * FROM users WHERE uid = :uid")
    fun getWithPrimaryKey(uid: String?): User?

    /**
     * Inserts a User into the database. If a conflict occurs, the existing entry will be replaced.
     *
     * @param user The User object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    /**
     * Updates an existing User in the database.
     *
     * @param user The User object to update.
     */
    @Update
    fun update(user: User)
}
