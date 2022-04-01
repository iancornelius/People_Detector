package iancornelius.camerax_people_detector.ui

import android.graphics.Rect
import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.sp

private const val TAG = "Boundaries"

class Boundaries {

    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = 32F
        color = android.graphics.Color.RED
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
    }

    @Composable
    fun displayBounds(objects: SnapshotStateList<HashMap<Int, Rect>>) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            for (obj in objects) {
                for (item in obj) {
                    Log.d(TAG, "Item: ${item.key} ${item.value}")
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "Object ID: ${item.key}",
                            item.value.centerX().toFloat(),
                            item.value.centerY().toFloat(),
                            textPaint
                        )
                    }
                    drawRect(
                        color = Color.Red,
                        size = Size(
                            item.value.width().toFloat(),
                            item.value.height().toFloat()
                        ),
                        topLeft = Offset(item.value.left.toFloat(), item.value.top.toFloat()),
                        style = Stroke(10f)
                    )
                    drawCircle(
                        color = Color.Red,
                        radius = 10F,
                        center = Offset(item.value.exactCenterX(), item.value.exactCenterY()),
                        style = Stroke(3f)
                    )
                }
            }
        }
    }

}