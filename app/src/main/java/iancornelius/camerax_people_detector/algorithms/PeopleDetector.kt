package iancornelius.camerax_people_detector.algorithms

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

private const val TAG = "PeopleDetector"

class PeopleDetector(private val onFaceDetected: (MutableList<Face>) -> Unit) : ImageAnalysis.Analyzer {
    val realTimeOpts = FaceDetectorOptions.Builder()
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .enableTracking()
        .build()

    private val highAccuracyOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .enableTracking()
        .build()

    private val detector = FaceDetection.getClient(highAccuracyOpts)

    private var running = false

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        if (running) {
            image.close()
            return
        }

        image.image?.let {
            running = true
            val mediaImage = image.image
            if(mediaImage != null) {
                val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
                detector.process(inputImage)
                    .addOnSuccessListener {
                        onFaceDetected.invoke(it)
                    }
                    .addOnCompleteListener {
                        running = false
                        image.close()
                    }
            }

        }
    }

}