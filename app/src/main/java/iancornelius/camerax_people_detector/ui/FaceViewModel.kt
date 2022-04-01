package iancornelius.camerax_people_detector.ui

import android.graphics.Rect
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.face.Face

private const val TAG = "FaceViewModel"

class FaceViewModel : ViewModel() {

//    var faceBoundingBox by mutableStateOf<Rect?>(null)
    var detectedFaces = mutableStateListOf<HashMap<Int, Rect>>()

    fun setFace(faces: MutableList<Face>) {
        detectedFaces.clear()
        val tmpDetections: HashMap<Int, Rect> = HashMap()
        for (face in faces) {
            face.trackingId?.let { tmpDetections.put(it, face.boundingBox) }
//            faceBoundingBox = face.boundingBox
        }
        detectedFaces.add(tmpDetections)
    }
}