package com.amitranofinzi.vimata.data.extensions

import androidx.room.TypeConverter
import com.google.firebase.Timestamp

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it / 1000, ((it % 1000) * 1000000).toInt()) }
    }
    @TypeConverter
    fun dateToTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.let { it.seconds * 1000 + it.nanoseconds / 1000000 }
    }
}
