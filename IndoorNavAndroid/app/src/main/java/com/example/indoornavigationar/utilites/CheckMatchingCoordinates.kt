package com.example.indoornavigationar.utilites

class CheckMatchingCoordinates {
    fun validateStartCoordinates(x: Double, y: Double): Boolean {
        var result = false
        var isCorrectStart = false
        var isCorrectTop = false

        if (x >= COORD_START_X && y >= COORD_START_Y)
            isCorrectStart = true
        else if (x >= COORD_TOP_X && y >= COORD_TOP_Y)
            isCorrectTop = true
        else
            result = false

        if (isCorrectStart || isCorrectTop) {
            result = validateEndCoordinates(x, y)
        }

        return result
    }

    private fun validateEndCoordinates(x: Double, y: Double): Boolean {
        var isCorrectEnd = false

        if (x <= COORD_END_X && y <= COORD_END_Y)
            isCorrectEnd = true

        return isCorrectEnd
    }
}