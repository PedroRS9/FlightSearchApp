package com.example.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query("SELECT * FROM airport ORDER BY id ASC")
    fun getAll(): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE (:searchField IS NULL OR IATA_code LIKE '%' || :searchField || '%') OR (:searchField IS NULL OR name LIKE '%' || :searchField || '%')")
    fun searchAll(searchField: String): Flow<List<Airport>>
}