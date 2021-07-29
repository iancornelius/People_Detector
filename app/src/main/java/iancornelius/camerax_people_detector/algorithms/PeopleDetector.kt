package iancornelius.camerax_people_detector.algorithms

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.PredefinedCategory
import java.nio.ByteBuffer

typealias PeopleListener = (luma: Unit) -> Unit

class PeopleDetector(listener: PeopleListener?= null) : ImageAnalysis.Analyzer {

    private val listeners = ArrayList<PeopleListener>().apply { listener?.let { add(it) } }

    val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableClassification()  // Optional
        .build()

    // High-accuracy landmark detection and face classification
    val highAccuracyOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    // Real-time contour detection
    val realTimeOpts = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .build()

    val objectDetector = ObjectDetection.getClient(options)

    val detector = FaceDetection.getClient(realTimeOpts)

    fun onFrameAnalyzed(listener: PeopleListener) = listeners.add(listener)

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    override fun analyze(image: ImageProxy) {
        // If there are no listeners attached, we don't need to perform analysis
        if (listeners.isEmpty()) {
            image.close()
            return
        }
        val objects = detectObjects(image)
        listeners.forEach { it(objects) }
        image.close()
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun detectObjects(image: ImageProxy) {
        val mediaImage = image.image
        if(mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            detector.process(inputImage).addOnSuccessListener { detected ->
                for(obj in detected) {
                    Log.d("PeopleDetector-Debug", "${obj.boundingBox}")
                }
            }
        }
        Log.d("PeopleDetector-Debug", "${mediaImage?.height}")
    }

}