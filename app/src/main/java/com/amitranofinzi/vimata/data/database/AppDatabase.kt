package com.amitranofinzi.vimata.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amitranofinzi.vimata.data.dao.ChatDao
import com.amitranofinzi.vimata.data.dao.RelationshipDao
import com.amitranofinzi.vimata.data.dao.TestDao
import com.amitranofinzi.vimata.data.dao.UserDao
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Exercise
import com.amitranofinzi.vimata.data.model.Message
import com.amitranofinzi.vimata.data.model.Relationship
import com.amitranofinzi.vimata.data.model.Test
import com.amitranofinzi.vimata.data.model.TestSet
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.model.Workout


@Database(
    entities = [
        User::class,
        Chat::class,
        Message::class,
        Relationship::class,
        Exercise::class,
        Workout::class,
        Test::class,
        TestSet::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    // functions to provide access to dao interface of each entity of db
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun testDao(): TestDao
    abstract fun relationshipDao(): RelationshipDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        /*
        * Gets the singleton instance of the database.
        *
        * @param context The application context.
        * @return The singleton instance of AppDatabase.
        */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

