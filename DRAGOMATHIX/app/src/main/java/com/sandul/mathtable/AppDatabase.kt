package com.sandul.mathtable

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(PlayerSettings::class), version = 1)
@TypeConverters(ProgressConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerSettingsDao() : PlayerSettingsDao
}