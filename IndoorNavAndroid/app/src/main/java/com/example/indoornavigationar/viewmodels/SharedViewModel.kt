package com.example.indoornavigationar.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.indoornavigationar.utilites.MapPoint

class SharedViewModel: ViewModel() {
    private val selectedLocationId = MutableLiveData<Int>().apply {
        value = -1
    }

    private val selectedMapPoint = MutableLiveData<MapPoint>().apply {
        value = null
    }

    fun setLocationId(id: Int) {
        selectedLocationId.value = id
    }

    fun getLocationId(): Int? {
        return selectedLocationId.value
    }

    fun setMapPoint(mapPoint: MapPoint) {
        selectedMapPoint.value = mapPoint
    }

    fun getMapPoint(): MapPoint? {
        return selectedMapPoint.value
    }
}