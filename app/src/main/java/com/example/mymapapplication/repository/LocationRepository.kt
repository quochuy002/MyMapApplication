package com.example.mymapapplication.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.mymapapplication.Instance
import com.example.mymapapplication.Location
import com.example.mymapapplication.dao.LocationDAO

class LocationRepository(app :Application) {
    private val locationDAO :LocationDAO
    init {
        val instance = Instance.getInstance()
        locationDAO = instance.getInstance(app).getLocationDao()
    }
    fun insertLocation(location: Location) = locationDAO.insertLocation(location)
    fun getAllLocation() : LiveData<List<Location>> = locationDAO.getAllLocation()
    fun getLimit10Element():LiveData<List<Location>> = locationDAO.getLimit10Element()

}