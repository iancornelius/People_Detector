package iancornelius.camerax_people_detector.ui

import android.graphics.Rect
import android.graphics.RectF
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
import kotlin.math.ceil

private const val TAG = "FaceBounds"

class FaceBounds {

    @Composable
    fun FaceBounds(bounds: Rect? = null) {
        Canvas(modifier = Modifier.fillMaxSize().background(Color.Transparent)) {
            bounds?.let {
                drawRect(
                    color = Color.Red,
                    size = Size(it.width().toFloat(), it.height().toFloat()),
                    topLeft = Offset(it.exactCenterY(), it.exactCenterX()),
                    style = Stroke(10f)
                )
                drawCircle(
                    color = Color.Red,
                    radius = 5F,
                    center = Offset(it.exactCenterX(), it.exactCenterY()),
                    style = Stroke(2.5f)
                )
            }
        }
    }

}