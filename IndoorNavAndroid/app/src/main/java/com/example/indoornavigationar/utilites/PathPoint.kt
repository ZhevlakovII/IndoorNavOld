package com.example.indoornavigationar.utilites

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PathPoint(
    val x: Double,
    val y: Double
) : Parcelable