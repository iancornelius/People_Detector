package iancornelius.camerax_people_detector.ui

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

private const val TAG = "ObjectBounds"

class ObjectBounds {

    @Composable
    fun ObjectBounds(bounds: Rect? = null) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)) {
            bounds?.let {

                // Draw the bounding box of the detected face
                drawRect(
                    color = Color.Red,
                    size = Size(it.width().toFloat(), it.height().toFloat()),
                    topLeft = Offset(it.left.toFloat(), it.top.toFloat()),
                    style = Stroke(10f)
                )

            }
        }
    }

}