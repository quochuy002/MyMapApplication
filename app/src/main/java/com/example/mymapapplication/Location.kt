package com.example.mymapapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locationTable")
data class Location(
    @ColumnInfo(name = "latLng")
    val latLng: Double,
    @ColumnInfo(name = "longLng")
    val longLng: Double,
    @ColumnInfo(name = "timeSnap")
    val timeSnap: Long = System.currentTimeMillis()
) : java.io.Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "locationId")
    var id: Int = 0

}