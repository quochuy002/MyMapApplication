package com.example.mymapapplication

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.mymapapplication.dao.LocationHelper

class Instance {
    @Volatile
    private var INSTANCE :LocationHelper? = null
    fun getInstance(app :Context):LocationHelper{
        if (INSTANCE == null){
            synchronized(this){
                INSTANCE = Room.databaseBuilder(app,LocationHelper::class.java,"my_location").build()
            }
        }
        return INSTANCE!!
    }
    companion object{
        private var INSTANCE :Instance? = null
        fun getInstance(): Instance{
            if (INSTANCE == null){
                INSTANCE = Instance()
            }
            return INSTANCE!!
        }
    }
}