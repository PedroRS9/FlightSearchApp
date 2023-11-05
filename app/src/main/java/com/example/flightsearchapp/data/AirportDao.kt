package com.example.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query("SELECT * FROM airport ORDER BY id ASC")
    fun getAll(): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE name LIKE '%' || :searchField || '%' OR iata_code LIKE :searchField")
    fun searchAll(searchField: String): Flow<List<Airport>>
}