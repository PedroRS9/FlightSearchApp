package com.example.flightsearchapp

import android.app.Application
import com.example.flightsearchapp.data.FlightDatabase

class FlightApplication : Application() {
    val database: FlightDatabase by lazy { FlightDatabase.getDatabase(this) }
}