package iancornelius.camerax_people_detector.algorithms

import android.graphics.Point
import androidx.compose.ui.geometry.Offset
import kotlin.math.roundToInt

class MovementHandler {

    var previousLocation = Point()
    var previousDirection = Point()

    fun ewma_center(curr: Point, alpha: Double): Point {
        val x = alpha * curr.x + (1 - alpha) * previousLocation.x
        val y = alpha * curr.y + (1 - alpha) * previousLocation.y
        return Point(x.roundToInt(), y.roundToInt())
    }
    
    fun get_direction(curr: Point): Point {
        if (previousLocation != null) {
            val dX = if (curr.x - previousLocation.x >= 1) {
                1
            } else if (curr.x - previousLocation.x <= -1) {
                -1
            } else {
                0
            }

            val dY = if (curr.y - previousLocation.y >= 1) {
                1
            } else if (curr.y - previousLocation.y <= -1) {
                -1
            } else {
                0
            }
            return Point(dX, dY)
        }
        return Point(-9, -9)

    }

}