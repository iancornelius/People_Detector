package iancornelius.camerax_people_detector

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import iancornelius.camerax_people_detector.algorithms.MarkovModel
import iancornelius.camerax_people_detector.algorithms.MovementHandler
import iancornelius.camerax_people_detector.algorithms.ObjectDetector
import iancornelius.camerax_people_detector.algorithms.PeopleDetector
import iancornelius.camerax_people_detector.ui.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ObjectViewModel>()
    private val viewModelFaces by viewModels<FaceViewModel>()

    private val markovModel: MarkovModel = MarkovModel()
    private val movementHandler: MovementHandler = MovementHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasPermissions(this.applicationContext)) {
            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
        } else {
            setViewContent()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                Toast.makeText(this.applicationContext, "Permission request granted", Toast.LENGTH_LONG).show()
                setViewContent()
            } else {
                Toast.makeText(this.applicationContext, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setViewContent() {
        setContent {
            CameraView().Show(ObjectDetector {viewModel.setObject(it)})
//            CameraView().Show(PeopleDetector {viewModelFaces.setFace(it)})
            Boundaries().displayBounds(viewModel.detectedObjects)
//            Boundaries().displayBounds(viewModelFaces.detectedFaces)

//            if (viewModel.objectBoundingBox != null) {
//                val currentLocation = Point(
//                    viewModel.objectBoundingBox!!.exactCenterX().roundToInt(),
//                    viewModel.objectBoundingBox!!.exactCenterY().roundToInt()
//                )
//                val ewmaLocation = movementHandler.ewma_center(currentLocation, 0.5)
//                val currentDirection = movementHandler.get_direction(ewmaLocation)
//
//                if (movementHandler.previousDirection != null || movementHandler.previousDirection != Point(-9, -9)) {
//                    markovModel.updateFrequencies(movementHandler.previousDirection, currentDirection)
//                    markovModel.updateProbabilities()
//                    val prediction = markovModel.generatePrediction(currentDirection)
//                    DirectionOverlay().drawDirection(viewModel.objectBoundingBox, currentDirection, prediction)
//                }
//                movementHandler.previousLocation = currentLocation
//                movementHandler.previousDirection = currentDirection
//            }
        }
    }
    companion object {
        const val PERMISSIONS_REQUEST_CODE = 10
        val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

}