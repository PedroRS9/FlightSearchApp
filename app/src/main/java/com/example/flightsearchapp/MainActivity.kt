package com.example.flightsearchapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.Html.fromHtml
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.Favorite
import com.example.flightsearchapp.data.Flight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var airportsLayout: LinearLayout
    private lateinit var searchEditText: EditText
    private lateinit var application: FlightApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = getApplication() as FlightApplication

        val mainLayout = createMainLayout()
        setContentView(mainLayout)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handleSearchTextChanged(s.toString())
            }
        })
    }

    private fun createMainLayout(): LinearLayout {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        layout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgreen))

        val titleTextView = TextView(this)
        titleTextView.text = "Flight App"
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24F)
        titleTextView.setTextColor(ContextCompat.getColor(this, R.color.red))
        titleTextView.gravity = Gravity.CENTER
        titleTextView.setPadding(0, 16, 0, 16)
        layout.addView(titleTextView)

        searchEditText = EditText(this)
        searchEditText.hint = "Search airports..."
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.textColor))
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.hintColor))
        layout.addView(searchEditText)

        airportsLayout = LinearLayout(this)
        airportsLayout.orientation = LinearLayout.VERTICAL
        layout.addView(airportsLayout)

        return layout
    }

    private fun handleSearchTextChanged(text: String) {
        airportsLayout.removeAllViews()

        if (text.isNotEmpty()) {
            showAirportsByText(text)
        } else {
            showFavorites()
        }
    }

    private fun showAirportsByText(text: String) {
        lifecycleScope.launch {
            try {
                val airportsFlow: Flow<List<Airport>> = application.database.airportDao().searchAll(text)
                airportsFlow.collect { airports ->
                    airports.forEach { airport ->
                        val airportView = createAirportView(airport)
                        airportsLayout.addView(airportView)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun showFavorites() {
        lifecycleScope.launch {
            try {
                val favoritesFlow: Flow<List<Favorite>> = application.database.favoriteDao().getAll()
                favoritesFlow.collect { favorites ->
                    airportsLayout.removeAllViews()
                    favorites.forEach { favorite ->
                        val airportView = createAirportView(
                            Airport(
                                id = -1,
                                iataCode = favorite.departureCode,
                                name = favorite.destinationCode,
                                passengers = 0
                            )
                        )
                        airportView.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.favoriteColor))
                        airportsLayout.addView(airportView)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun createAirportView(airport: Airport): View {
        val airportView = TextView(this)
        airportView.text = fromHtml("<b>${airport.name}</b> - <b>${airport.iataCode}</b>", 0)
        airportView.setTextColor(ContextCompat.getColor(this, R.color.textColor))
        airportView.gravity = Gravity.CENTER_VERTICAL
        airportView.setPadding(16, 16, 16, 16)

        airportView.setOnClickListener {
            val dummyFlights = generateDummyFlights(airport)
            val flightsLayout = createFlightsLayout(dummyFlights)
            showFlightsDialog(flightsLayout)
        }
        airportView.setOnTouchListener { v, event ->
            handleAirportTouch(v, event)
        }
        return airportView
    }

    private fun handleAirportTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                view.setBackgroundColor(ContextCompat.getColor(this, R.color.touchColor))
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                view.setBackgroundColor(Color.TRANSPARENT)
            }
        }
        return false
    }

    private fun generateDummyFlights(airport: Airport): List<Flight> {
        val flights = mutableListOf<Flight>()
        for (i in 1..5) {
            flights.add(
                Flight(
                    originAirport = airport.iataCode,
                    destinationAirport = "DEST$i",
                    flightName = "Flight $i"
                )
            )
        }
        return flights
    }

    private fun createFlightsLayout(flights: List<Flight>): LinearLayout {
        val flightsLayout = LinearLayout(this)
        flightsLayout.orientation = LinearLayout.VERTICAL
        flights.forEach { flight ->
            val flightView = createFlightView(flight)
            flightsLayout.addView(flightView)
        }
        return flightsLayout
    }

    private fun createFlightView(flight: Flight): TextView {
        val flightView = TextView(this)
        val flightInfo = "${flight.flightName} (Origin: ${flight.originAirport}, Destination: ${flight.destinationAirport})"
        flightView.text = flightInfo
        flightView.setTextColor(ContextCompat.getColor(this, R.color.textColor))
        flightView.setPadding(16, 8, 16, 8)
        flightView.setOnClickListener {
            handleFlightClick(it as TextView, flight)
        }
        return flightView
    }

    private fun handleFlightClick(textView: TextView, flight: Flight) {
        val text = textView.text.toString()
        println("You selected flight: $text")

        textView.setBackgroundColor(ContextCompat.getColor(this, R.color.favoriteColor))

        val favorite = Favorite(
            id = 0,
            destinationCode = flight.destinationAirport,
            departureCode = flight.originAirport
        )
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {

            }
        }
    }

    private fun showFlightsDialog(flightsLayout: LinearLayout) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Flights")
            .setView(flightsLayout)
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }
}