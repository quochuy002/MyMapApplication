package com.example.mymapapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mymapapplication.databinding.ActivityMapsBinding
import com.example.mymapapplication.service.MyService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.utsman.samplegooglemapsdirection.kotlin.model.DirectionResponses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider(
            this, LocationViewModel.LocationFactory(
                this.application
            )
        )[LocationViewModel::class.java]
    }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var client: FusedLocationProviderClient
    private var listLimitLocation = mutableListOf<Location>()
    var mapFragment: SupportMapFragment? = null

    companion object {
        const val CODE = 101
    }


    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync(this)
        client = LocationServices.getFusedLocationProviderClient(this)
        binding.fabLocation.setOnClickListener {
            startActivity(Intent(this, ListLocationActivity::class.java))
        }
        locationViewModel.getLimit10Element().observe(this) {
            listLimitLocation = it.toMutableList()
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ),
                    CODE
                )
            }
            return
        } else {
            getMyLocation()
            val intent = Intent(this, MyService::class.java)
            startService(intent)
        }
    }


    private fun getMyLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
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
        val task = client.lastLocation
        task.addOnSuccessListener {
            it?.let { location ->
//                Log.e("huhu", "getMyLocation: $sydney")

                Log.e("TAG", "getMyLocation: ${locationViewModel.getAllLocation()}")
//                val polyline = PolylineOptions().apply {
//                    listLimitLocation.forEach { mLocation ->
//                        add(
//                            LatLng(mLocation.latLng, mLocation.longLng),
//                            LatLng(mLocation.latLng, mLocation.longLng)
//                        )
//                        //add(LatLng(-34.0,151.0))
//                    }
//                    color(R.color.teal_200)
//                    width(10F)
//                }
//                listLimitLocation.forEach {
//                    val pos = MarkerOptions().position(LatLng(it.latLng, it.longLng))
//                    val url: String = getDirectionUrl(pos.position, "driving")
//                }
//                mMap.addPolyline(polyline)

                listLimitLocation?.forEach { pos ->
                    val sydney = LatLng(pos.latLng, pos.longLng)
                    val sydneyFirst = LatLng(listLimitLocation[0].latLng, listLimitLocation[0].longLng)
                    val image = BitmapFactory.decodeResource(resources,R.drawable.image)
                    val resize = Bitmap.createScaledBitmap(image,30,30,true)
                    val bitmap = BitmapDescriptorFactory.fromBitmap(resize)
                    mMap.addMarker(MarkerOptions().position(sydney).title("Your location ${pos.id}").icon(bitmap))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 19F))
//                    val mLocation = it.latitude.toString() + "," + it.longitude.toString()
//                    val anotherLocation = pos.latLng.toString() + "," + it.longitude.toString()
////                    getLineDirection(mLocation, anotherLocation,"AIzaSyAaTowrDkUczYpWMi1Z4-kAz2KZ9NEfq5E", pos = listLimitLocation.size)

                }
            }

        }
    }

//    private fun getDirectionUrl(position: LatLng, derection: String): String {
//        val strOrigin = "origin" + position.latitude + "," + position.longitude
//        val strDest = "destination" + position.latitude + "," + position.longitude
//
//        val mode = "mode$derection"
//        val parameters = "$position&$position$mode"
//        val output = "json"
//
//        val url =
//            "https://maps.googleapis.com/maps/api/directions$output?$parameters&key=AIzaSyAaTowrDkUczYpWMi1Z4-kAz2KZ9NEfq5E"
//
//        return url
//    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    fun getLineDirection(
        mLocation: String,
        anotherLocation: String,
        key: String,
        pos: Int
    ) {
        val apiservice: Call<DirectionResponses> = RetrofitClient.apiservice.getDirection(
            origin = mLocation,
            destination = anotherLocation,
            apiKey = key
        )
        apiservice.enqueue(object : Callback<DirectionResponses> {
            override fun onResponse(
                call: Call<DirectionResponses>,
                response: Response<DirectionResponses>
            ) {
          //      drawPolyline(response, pos)
                Log.e("asdasd", "onResponse: okok ${response}")
            }

            override fun onFailure(call: Call<DirectionResponses>, t: Throwable) {
            }

        })
    }

    private fun drawPolyline(response: Response<DirectionResponses>,pos :Int) {
        val shape = response.body()?.routes?.get(pos)?.overviewPolyline?.points
        val polyline = PolylineOptions().addAll(PolyUtil.decode(shape))
            .width(8f)
            .color(Color.RED)
        mMap.addPolyline(polyline)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CODE -> {
                for (grantResult in grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        getMyLocation()
                    }
                }
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        getMyLocation()
    }


}


