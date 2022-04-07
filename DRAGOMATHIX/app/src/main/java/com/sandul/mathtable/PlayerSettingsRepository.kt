package com.sandul.mathtable

import android.content.Context
import androidx.room.Room

private const val DATABASE_NAME = "playset.db"

class PlayerSettingsRepository private constructor(context: Context) {

    private val database : AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
    ).allowMainThreadQueries().build()

    val playerSettingsDao = database.playerSettingsDao()

    companion object {
        private var INSTANCE: PlayerSettingsRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = PlayerSettingsRepository(context)
            }
        }
        fun get(): PlayerSettingsRepository {
            return INSTANCE ?:
            throw IllegalStateException("PlayerSettings must be initialized")
        }
    }
}
