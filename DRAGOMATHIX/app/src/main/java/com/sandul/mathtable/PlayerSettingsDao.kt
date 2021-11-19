package com.sandul.mathtable

import androidx.room.*

@Dao
interface PlayerSettingsDao {

    @Query("SELECT * FROM playersettings")
    fun getAll() : List<PlayerSettings>

    @Query("SELECT * FROM playersettings WHERE pid = :id")
    fun getById(id: Int): PlayerSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(playersettings: PlayerSettings?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(playersettings: PlayerSettings?)

    @Delete
    fun delete(playersettings: PlayerSettings?)
}