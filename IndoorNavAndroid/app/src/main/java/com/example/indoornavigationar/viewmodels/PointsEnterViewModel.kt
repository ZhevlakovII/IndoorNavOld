package com.example.indoornavigationar.viewmodels

import android.app.Application
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.indoornavigationar.R
import com.example.indoornavigationar.database.navlocations.NavLocations
import com.example.indoornavigationar.database.navlocations.NavLocationsRepo
import com.example.indoornavigationar.utilites.MapPoint
import com.example.indoornavigationar.utilites.PathCreator
import dagger.hilt.android.lifecycle.HiltViewModel
import ovh.plrapps.mapcompose.api.*
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.state.MapState
import javax.inject.Inject

@HiltViewModel
class PointsEnterViewModel @Inject internal constructor(
    private val application: Application,
    private val navLocationsRepo: NavLocationsRepo,
    private val pathCreator: PathCreator
) : ViewModel() {
    lateinit var searchedItem: LiveData<NavLocations>

    private val listOfPickedPoints: MutableList<MapPoint> = MutableList(2) { MapPoint(0.0, 0.0, "none") }
    private var isUserPointActive = false
    private var isDestPointActive = false

    val dotsCount: MutableLiveData<Int> = MutableLiveData<Int>().apply {
        value = 0
    }

    private val tileStreamProvider =
        TileStreamProvider { row, col, zoomLvl ->
            try {
                application.applicationContext.assets.open("homePlan/$zoomLvl/$row/$col.png")
            } catch (e: Exception) {
                null
            }
        }

    val state: MapState by mutableStateOf(
        MapState(3, 1024, 1024).apply {
            addLayer(tileStreamProvider)
            enableRotation()
        }
    )

    private fun addMarkerOnMap(x: Double, y: Double, numOfPressedButton: Int) {
        if (numOfPressedButton == 1 ) {
            if (isUserPointActive) {
                state.removeMarker("${numOfPressedButton - 1}")
                dotsCount.value = dotsCount.value?.minus(1)
            }

            state.addMarker("${numOfPressedButton - 1}", x, y) {
                Icon(
                    painter = painterResource(id = R.drawable.user_point),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = Color(R.color.interface_tertiary)
                )

                isUserPointActive = true
                dotsCount.value = dotsCount.value?.plus(1)
            }
        } else if (numOfPressedButton == 2) {
            if (isDestPointActive) {
                state.removeMarker("${numOfPressedButton - 1}")
                dotsCount.value = dotsCount.value?.minus(1)
            }

            state.addMarker("${numOfPressedButton - 1}", x, y) {
                Icon(
                    painter = painterResource(id = R.drawable.navigate_dot_icon),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color(R.color.interface_primary)
                )

                isDestPointActive = true
                dotsCount.value = dotsCount.value?.plus(1)
            }
        }
    }

    fun getItemFromLocations(id: Int) {
        searchedItem = navLocationsRepo.getItemById(id).asLiveData()
    }

    fun addMapPointToList(mapPoint: MapPoint) {
        listOfPickedPoints[1] = mapPoint
        addMarkerOnMap(mapPoint.x, mapPoint.y, 2)
    }

    fun addSearchedMapPointToList(x: Double, y: Double, name:String, numOfPressedButton: Int) {
        when (numOfPressedButton) {
            1 -> {
                listOfPickedPoints[0] = MapPoint(x, y, name)

                addMarkerOnMap(x, y, numOfPressedButton)
            }
            2 -> {
                listOfPickedPoints[1] = MapPoint(x, y, name)

                addMarkerOnMap(x, y, numOfPressedButton)
            }
        }
    }

    fun createPath() {
        val pointsList = pathCreator.createPath(listOfPickedPoints)

        with(state) {
            val builder = makePathDataBuilder()

            for (point in pointsList)
                builder.addPoint(point.x, point.y)

            val pathData = builder.build() ?: return@with

            addPath(
                "track",
                pathData,
                color = Color(R.color.interface_tertiary),
                width = 10.dp
            )
        }
    }
}