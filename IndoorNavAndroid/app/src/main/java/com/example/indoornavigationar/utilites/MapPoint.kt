package com.example.indoornavigationar.utilites

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapPoint(
    val x: Double,
    val y: Double,
    val name: String
) : Parcelable