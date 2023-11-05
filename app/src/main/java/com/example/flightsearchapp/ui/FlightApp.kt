package com.example.flightsearchapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightApp(
    viewModel: MainViewModel = viewModel(factory = MainViewModel.factory)
){
    var departureCity by remember { mutableStateOf("") }
    var arrivalCity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Buscar vuelos", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = departureCity,
            onValueChange = { departureCity = it },
            label = { Text("Ciudad de salida") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = arrivalCity,
            onValueChange = { arrivalCity = it },
            label = { Text("Ciudad de llegada") }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { /* Realizar la búsqueda de vuelos aquí */ }) {
            Text("Buscar")
        }
    }
}