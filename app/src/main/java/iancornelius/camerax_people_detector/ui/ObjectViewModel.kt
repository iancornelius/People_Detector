package iancornelius.camerax_people_detector.ui

import android.content.Context
import android.graphics.Rect
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.objects.DetectedObject
import java.io.BufferedReader

private const val TAG = "ObjectDetector"

class ObjectViewModel : ViewModel() {

//    var objectBoundingBox = mutableStateListOf<Rect>()
    var detectedObjects = mutableStateListOf<HashMap<Int, HashMap<Int, Rect>>>()



    fun setObject(objects: MutableList<DetectedObject>) {
        detectedObjects.clear()
        val tmpDetections: HashMap<Int, HashMap<Int, Rect>> = HashMap()
        for (obj in objects) {
            val tmpData: HashMap<Int, Rect> = HashMap()
            for (lbl in obj.labels) {
                Log.d(TAG, "Label: ${lbl.index} Rect: ${obj.boundingBox}")
                tmpData[lbl.index] = obj.boundingBox
                obj.trackingId?.let { tmpDetections.put(it, tmpData)}
            }
//            obj.trackingId?.let { tmpDetections.put(it, obj.boundingBox) }
//            Log.d(TAG, "Tracking ID: ${obj.trackingId}")
//            objectBoundingBox.add(obj.boundingBox)
        }
        detectedObjects.add(tmpDetections)
    }
}