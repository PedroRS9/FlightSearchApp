package com.example.flightsearchapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearchapp.FlightApplication
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.AirportDao
import com.example.flightsearchapp.data.Favorite
import com.example.flightsearchapp.data.FavoriteDao
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val airportDao: AirportDao, private val favoriteDao: FavoriteDao) : ViewModel() {
    fun searchAirports(searchField: String) : Flow<List<Airport>> {
        return airportDao.searchAll(searchField);
    }

    fun getFavorites() : Flow<List<Favorite>>{
        return favoriteDao.getAll()
    }

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory{
            initializer {
                val application = (this[APPLICATION_KEY] as FlightApplication)
                MainViewModel(application.database.airportDao(), application.database.favoriteDao())
            }
        }
    }
}