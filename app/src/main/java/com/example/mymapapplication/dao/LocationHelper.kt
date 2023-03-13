package com.example.mymapapplication.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymapapplication.Location

@Database(entities = [Location::class], version = 2)
abstract class LocationHelper :RoomDatabase() {
    abstract fun getLocationDao():LocationDAO
}