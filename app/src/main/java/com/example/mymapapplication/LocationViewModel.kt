package com.example.mymapapplication

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mymapapplication.repository.LocationRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LocationViewModel(app :Application) :ViewModel() {
    private val locationRepository =LocationRepository(app)

    fun insertLocation(location: Location){
        viewModelScope.launch {
            locationRepository.insertLocation(location)
            Log.e("TAG", "them vi tri thanh cong ", )
            Log.e("TAG", "insertLocation: $location", )
        }
    }
    fun getAllLocation() = locationRepository.getAllLocation()
    fun getLimit10Element() = locationRepository.getLimit10Element()

    @Suppress("UNCHECKED_CAST")
    class LocationFactory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
                return LocationViewModel(application) as T
            }
            throw  IllegalArgumentException("Unable construct viewModel")
        }
    }
}