package com.example.mymapapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mymapapplication.Location

@Dao
interface LocationDAO {
    @Insert
    fun insertLocation(location :Location)

    @Query("SELECT * FROM locationTable")
    fun getAllLocation(
    ): LiveData<List<Location>>

    @Query("SELECT * FROM locationTable ORDER BY timeSnap DESC LIMIT 10")
    fun getLimit10Element() :LiveData<List<Location>>
}