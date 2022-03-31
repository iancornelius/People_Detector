package iancornelius.camerax_people_detector.algorithms

import android.graphics.Point
import androidx.compose.ui.geometry.Offset

class MovementHandler {

    var previous = mutableListOf<Point>()

    fun ewma_center(prev: Offset, curr: Offset): Offset {
        val x = 0.75 * curr.x + (1 - 0.75) * prev.x
        val y = 0.75 * curr.y + (1 - 0.75) * prev.y
        return Offset(x.toFloat(), y.toFloat())
    }
    
    fun get_direction(prev: Point, curr: Point): Point {
        val dX = if (curr.x - prev.x >= 1) {
            1
        } else if (curr.x - prev.x <= -1) {
            -1
        } else {
            0
        }

        val dY = if (curr.y - prev.y >= 1) {
            1
        } else if (curr.y - prev.y <= -1) {
            -1
        } else {
            0
        }

        return Point(dX, dY)
    }

}