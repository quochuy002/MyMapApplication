package com.example.mymapapplication.service

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.mymapapplication.Instance
import com.example.mymapapplication.Location
import com.example.mymapapplication.dao.LocationDAO
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MyService() : Service() {

    companion object {
        val TAG = MyService::class.simpleName
    }

    private lateinit var locationDAO: LocationDAO


    private var jobCancelled: Boolean? = true
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setRuntimeSaveLocation()
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    private fun setRuntimeSaveLocation() {
//        object : CountDownTimer(6000, 1000) {
//            override fun onTick(p0: Long) {
//                Log.e(TAG, "onTick: $p0")
//            }
//            override fun onFinish() {
//                getMyLocation()
//                Log.e(TAG, "onFinish: Hoan thanh")
//                start()
//            }
//        }.start()
        getMyLocation()

    }

    private fun getMyLocation() {
        val instance = Instance.getInstance().getInstance(applicationContext).getLocationDao()
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            it?.let { location ->
                Log.e(TAG, "getMyLocation: $location")
                val sydney = LatLng(location.latitude, location.longitude)
                val lastLocation = Location(21.0332355,105.7337709,getTimeSnap())
                CoroutineScope(Dispatchers.IO).launch {
                    instance.insertLocation(lastLocation)
                }

            }

        }
    }
    private fun getTimeSnap():Long{
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val currentTimestamp = dateFormat.format(currentDate)
        return dateFormat.parse(currentTimestamp)?.time ?: 0L
    }


}