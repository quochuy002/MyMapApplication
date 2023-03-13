package com.example.mymapapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymapapplication.adapter.ListLimitLocationAdapter
import com.example.mymapapplication.adapter.ListLocationAdapter

class ListLocationActivity : AppCompatActivity() {
    private val rcvListLocation: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rcv_list_location) }
    private val rcvLimitElement: RecyclerView by lazy { findViewById<RecyclerView>(R.id.rcv_limit_element) }
    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider(
            this, LocationViewModel.LocationFactory(
                this.application
            )
        )[LocationViewModel::class.java]
    }
    private val listLocationAdapter :ListLocationAdapter by lazy {
        ListLocationAdapter(this)
    }
    private val listLimitLocationAdapter :ListLimitLocationAdapter by lazy {
        ListLimitLocationAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_location)
        rcvListLocation.apply {
            locationViewModel.getAllLocation().observe(this@ListLocationActivity){
                listLocationAdapter.listLocation = it as MutableList<Location>
            }
            layoutManager  = LinearLayoutManager(this@ListLocationActivity)
            adapter = listLocationAdapter
        }

        rcvLimitElement.apply {
            locationViewModel.getLimit10Element().observe(this@ListLocationActivity){
                listLimitLocationAdapter.listLimitLocation = it as MutableList<Location>
            }
            layoutManager  = LinearLayoutManager(this@ListLocationActivity)
            adapter = listLimitLocationAdapter
        }

    }

}