package com.sandul.mathtable

import androidx.room.TypeConverter
import java.util.*

class ProgressConverter {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun fromProgress(progress: List<Int>): String {
        return progress.joinToString(separator = ",")
    }

    @TypeConverter
    fun toProgress(data: String): List<Int>? {
        return data.split(",").map { it.toInt() }.toList()
    }
}