package iancornelius.camerax_people_detector.ui

import android.graphics.Point
import android.graphics.Rect
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

private const val TAG = "DirectionOverlay"

class DirectionOverlay {

    @Composable
    fun drawDirection(bounds: Rect? = null, direction: Point, prediction: Point) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)) {
            bounds?.let {

                val dX = when (direction.x) {
                    -1 -> {
                        -325
                    }
                    1 -> {
                        +325
                    }
                    else -> {
                        0
                    }
                }

                val dY = when (direction.y) {
                    -1 -> {
                        -325
                    }
                    1 -> {
                        +325
                    }
                    else -> {
                        0
                    }
                }

                val pX = when (prediction.x) {
                    -1 -> {
                        -350
                    }
                    1 -> {
                        +350
                    }
                    else -> {
                        0
                    }
                }

                val pY = when (prediction.y) {
                    -1 -> {
                        -325
                    }
                    1 -> {
                        +325
                    }
                    else -> {
                        0
                    }
                }

                // Draw the predicted direction of movement
                drawLine(
                    color = Color.Blue,
                    start = Offset(it.exactCenterX(), it.exactCenterY()),
                    end = Offset(it.exactCenterX() + pX, it.exactCenterY() + pY),
                    strokeWidth = 10f
                )

                // Draw the direction of movement
                drawLine(
                    color = Color.Red,
                    start = Offset(it.exactCenterX(), it.exactCenterY()),
                    end = Offset(it.exactCenterX() + dX, it.exactCenterY() + dY),
                    strokeWidth = 10f
                )

            }
        }
    }

}