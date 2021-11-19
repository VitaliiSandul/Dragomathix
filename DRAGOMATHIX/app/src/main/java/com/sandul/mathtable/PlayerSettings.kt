package com.sandul.mathtable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity
class PlayerSettings(
        @PrimaryKey val pid: Int = 0,
        @ColumnInfo(name="language") val language: String?,
        @ColumnInfo(name="gender") val gender: String?,
    @TypeConverters(ProgressConverter::class)
    var date: Date = Date(),
    @TypeConverters(ProgressConverter::class)
    var progress: List<Int>? = null
)
