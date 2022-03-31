package iancornelius.camerax_people_detector.ui

import android.graphics.Rect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.objects.DetectedObject

class ObjectViewModel : ViewModel() {
    var objectBoundingBox by mutableStateOf<Rect?>(null)

    fun setObject(objects: MutableList<DetectedObject>) {
        for (obj in objects) {
            objectBoundingBox = obj.boundingBox
        }
    }
}