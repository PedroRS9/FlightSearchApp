package com.example.flightsearchapp.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey val id: Int,
    @NonNull val name: String,
    @NonNull @ColumnInfo(name="iata_code") val iataCode: String,
    @NonNull val passengers: Int
)