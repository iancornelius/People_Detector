package iancornelius.camerax_people_detector.algorithms

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

private const val TAG = "ObjectDetector"

class ObjectDetector(private val onObjectDetected: (MutableList<DetectedObject>) -> Unit) : ImageAnalysis.Analyzer {

    private val realTimeOpts = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableMultipleObjects()
        .build()


    // PEDESTRIAN DETECTION EXAMPLE
    val localModel = LocalModel.Builder()
        .setAssetFilePath("inception_v4_299_quant.tflite")
        .build()

    val customObjectDetectorOptions =
        CustomObjectDetectorOptions.Builder(localModel)
            .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
            .enableClassification()
            .setClassificationConfidenceThreshold(0.5f)
            .setMaxPerObjectLabelCount(3)
            .build()


    private val detector = ObjectDetection.getClient(customObjectDetectorOptions)

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
                        Log.d(TAG, "Size of Objects List: ${it.size}")
                        onObjectDetected.invoke(it)
                    }
                    .addOnCompleteListener {
                        running = false
                        image.close()
                    }
            }

        }
    }

}