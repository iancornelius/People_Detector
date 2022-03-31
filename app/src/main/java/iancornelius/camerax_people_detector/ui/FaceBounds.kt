package iancornelius.camerax_people_detector.ui

import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import iancornelius.camerax_people_detector.algorithms.MarkovModel
import iancornelius.camerax_people_detector.algorithms.MovementHandler
import kotlin.math.roundToInt

private const val TAG = "FaceBounds"

class FaceBounds {

    @Composable
    fun FaceBounds(bounds: Rect? = null) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)) {
            bounds?.let {

                // Draw the bouding box of the detected face
                drawRect(
                    color = Color.Red,
                    size = Size(it.width().toFloat(), it.height().toFloat()),
                    topLeft = Offset(it.left.toFloat(), it.top.toFloat()),
                    style = Stroke(10f)
                )

                // Draw the centre of the detected face
                drawCircle(
                    color = Color.Red,
                    radius = 5F,
                    center = Offset(it.exactCenterX(), it.exactCenterY()),
                    style = Stroke(2.5f)
                )

                // Draw the predicted direction
                drawLine(
                    color = Color.Blue,
                    start = Offset(it.exactCenterX(), it.exactCenterY()),
                    end = Offset(it.exactCenterX() + 400, it.exactCenterY()),
                    strokeWidth = 10f
                )

                // Draw the direction of movement
                drawLine(
                    color = Color.Red,
                    start = Offset(it.exactCenterX(), it.exactCenterY()),
                    end = Offset(it.exactCenterX() + 325, it.exactCenterY()),
                    strokeWidth = 10f
                )

            }
        }
    }

}