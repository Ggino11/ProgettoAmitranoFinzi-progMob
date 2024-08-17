package com.amitranofinzi.vimata.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amitranofinzi.vimata.data.dao.ChatDao
import com.amitranofinzi.vimata.data.dao.CollectionDao
import com.amitranofinzi.vimata.data.dao.ExerciseDao
import com.amitranofinzi.vimata.data.dao.MessageDao
import com.amitranofinzi.vimata.data.dao.RelationshipDao
import com.amitranofinzi.vimata.data.dao.TestDao
import com.amitranofinzi.vimata.data.dao.TestSetDao
import com.amitranofinzi.vimata.data.dao.UserDao
import com.amitranofinzi.vimata.data.dao.WorkoutDao
import com.amitranofinzi.vimata.data.extensions.Converters
import com.amitranofinzi.vimata.data.model.Chat
import com.amitranofinzi.vimata.data.model.Collection
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
        Collection::class,
        Workout::class,
        Test::class,
        TestSet::class
    ],
    version = 1,
    exportSchema = false

)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // functions to provide access to dao interface of each entity of db
    abstract fun userDao(): UserDao
    abstract fun chatDao(): ChatDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun messageDao(): MessageDao
    abstract fun testDao(): TestDao
    abstract fun testSetDao(): TestSetDao
    abstract fun relationshipDao(): RelationshipDao
    abstract fun collectionDao(): CollectionDao
    abstract fun exerciseDao(): ExerciseDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "app-database")
                .fallbackToDestructiveMigration()
                .build()
    }
}

