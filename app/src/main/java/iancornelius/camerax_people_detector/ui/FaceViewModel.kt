package iancornelius.camerax_people_detector.ui

import android.graphics.Rect
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.face.Face

class FaceViewModel : ViewModel() {
    var faceBoundingBox by mutableStateOf<Rect?>(null)

    fun setFace(faces: MutableList<Face>) {
        Log.d("FaceViewModel", "${faces}")
        for (face in faces) {
            faceBoundingBox = face.boundingBox
        }
    }
}