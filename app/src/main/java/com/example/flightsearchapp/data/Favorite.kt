package com.example.flightsearchapp.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name="departure_code") val departureCode: String,
    @NonNull @ColumnInfo(name="destination_code") val destinationCode: String
)