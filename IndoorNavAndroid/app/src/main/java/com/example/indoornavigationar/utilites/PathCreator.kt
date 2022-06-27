package com.example.indoornavigationar.utilites

import android.app.Application
import javax.inject.Inject

class PathCreator @Inject internal constructor(
    private val application: Application
) {
    private var isReversed = false

    fun createPath(pointList: MutableList<MapPoint>): List<PathPoint> {
        val startPointName = pointList[0].name
        val endPointName = pointList[1].name
        val path: String?
        isReversed = false

        if (startPointName == endPointName) {
            return listOf(
                PathPoint(pointList[0].x, pointList[0].y),
                PathPoint(pointList[1].x, pointList[1].y)
            )
        } else {
            path = validateStartDest(startPointName, endPointName)
        }

        var pathPoints = mutableListOf<PathPoint>()

        if (path != null) {
            if (isReversed) {
                pathPoints.add(PathPoint(pointList[1].x, pointList[1].y))
                pathPoints = addPointsWithFile(pathPoints, path)
                pathPoints.add(PathPoint(pointList[0].x, pointList[0].y))
            } else {
                pathPoints.add(PathPoint(pointList[0].x, pointList[0].y))
                pathPoints = addPointsWithFile(pathPoints, path)
                pathPoints.add(PathPoint(pointList[1].x, pointList[1].y))
            }
        }

        return pathPoints
    }

    private fun validateStartDest(startPoint: String?, endPoint: String?): String? {
        return when (startPoint) {
            "Balcony" -> {
                val res = validateIfBalc(endPoint)
                if (res != null)
                    res
                else {
                    isReversed = true
                    validateStartDest(endPoint, startPoint)
                }
            }
            "Bedroom Y" -> {
                val res = validateIfBedY(endPoint)
                if (res != null)
                    res
                else {
                    isReversed = true
                    validateStartDest(endPoint, startPoint)
                }
            }
            "Bedroom GM" -> {
                val res = validateIfBedGM(endPoint)
                if (res != null)
                    res
                else {
                    isReversed = true
                    validateStartDest(endPoint, startPoint)
                }
            }
            "Bedroom M" -> {
                val res = validateIfBedM(endPoint)
                if (res != null)
                    res
                else {
                    isReversed = true
                    validateStartDest(endPoint, startPoint)
                }
            }
            "Living Room" -> {
                val res = validateIfLiv(endPoint)
                if (res != null)
                    res
                else {
                    isReversed = true
                    validateStartDest(endPoint, startPoint)
                }
            }
            "Corridor" -> {
                val res = validateIfCorr(endPoint)
                if (res != null)
                    res
                else {
                    isReversed = true
                    validateStartDest(endPoint, startPoint)
                }
            }
            "Kitchen" -> {
                when(endPoint) {
                    "Bathroom" -> "path/kitTo/kitchToBath.txt"
                    "Toilet" -> "path/kitTo/kitchToToil.txt"
                    else -> {
                        isReversed = true
                        validateStartDest(endPoint, startPoint)
                    }
                }
            }
            "Toilet" -> {
                when(endPoint) {
                    "Bathroom" -> "path/toilTo/toilToBath.txt"
                    else -> {
                        isReversed = true
                        validateStartDest(endPoint, startPoint)
                    }
                }
            }
            "Bathroom" -> {
                isReversed = false
                validateStartDest(endPoint, startPoint)
            }
            else -> null
        }
    }

    private fun validateIfBedM(endPoint: String?): String? {
        return when (endPoint) {
            "Bathroom" -> "path/bedMTo/bedMToBath.txt"
            "Bedroom GM" -> "path/bedMTo/bedMToBedGM.txt"
            "Bedroom Y" -> "path/bedMTo/bedMToBedY.txt"
            "Living Room" -> "path/bedMTo/bedMToLiv"
            "Corridor" -> "path/bedMTo/bedMToCorr.txt"
            "Kitchen" -> "path/bedMTo/bedMToKitch.txt"
            "Toilet" -> "path/bedMTo/bedMToToil.txt"
            else -> null
        }
    }

    private fun validateIfLiv(endPoint: String?): String? {
        return when (endPoint) {
            "Bathroom" -> "path/livTo/livToBath.txt"
            "Bedroom GM" -> "path/livTo/livToBedGM.txt"
            "Bedroom Y" -> "path/livTo/livToBedY.txt"
            "Corridor" -> "path/livTo/livToCorr.txt"
            "Kitchen" -> "path/livTo/livToKitch.txt"
            "Toilet" -> "path/livTo/livToToil.txt"
            else -> null
        }
    }

    private fun validateIfBedGM(endPoint: String?): String? {
        return when (endPoint) {
            "Bathroom" -> "path/bedGMTo/bedGMToBath.txt"
            "Bedroom Y" -> "path/bedGMTo/bedGMToBedY.txt"
            "Kitchen" -> "path/bedGMTo/bedGMToTo.txt"
            "Toilet" -> "path/bedGMTo/bedGMToToil.txt"
            else -> null
        }
    }

    private fun validateIfBalc(endPoint: String?): String? {
        return when (endPoint) {
            "Bathroom" -> "path/balcTo/balcToBath.txt"
            "Bedroom GM" -> "path/balcTo/balcToBedGM.txt"
            "Bedroom Y" -> "path/balcTo/balcToBedY.txt"
            "Bedroom M" -> "path/balcTo/balcToBedM"
            "Living Room" -> "path/balcTo/balcToLiv"
            "Corridor" -> "path/balcTo/balcToCorr.txt"
            "Kitchen" -> "path/balcTo/balcToKitch.txt"
            "Toilet" -> "path/balcTo/balcToToil.txt"
            else -> null
        }
    }

    private fun validateIfBedY(endPoint: String?): String? {
        return when (endPoint) {
            "Bathroom" -> "path/bedYTo/bedYToBath.txt"
            "Kitchen" -> "path/bedYTo/bedYTo.txt"
            "Toilet" -> "path/bedYTo/bedYToToil.txt"
            else -> null
        }
    }

    private fun validateIfCorr(endPoint: String?): String? {
        return when (endPoint) {
            "Bathroom" -> "path/corrTo/corrToBath.txt"
            "Bedroom GM" -> "path/corrTo/corrToBedGM.txt"
            "Bedroom Y" -> "path/corrTo/corrToBedY.txt"
            "Kitchen" -> "path/corrTo/corrToKitch.txt"
            "Toilet" -> "path/corrTo/corrToToil.txt"
            else -> null
        }
    }

    private fun addPointsWithFile(pathList: MutableList<PathPoint>, path: String): MutableList<PathPoint> {
        val points = application.assets?.open(path)?.bufferedReader()?.lineSequence() ?: return pathList

        for (point in points) {
            val value = point.split(',')
            pathList.add(PathPoint(value[0].toDouble(), value[1].toDouble()))
        }

        return pathList
    }
}