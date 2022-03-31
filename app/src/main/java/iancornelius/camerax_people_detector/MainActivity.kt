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
import iancornelius.camerax_people_detector.algorithms.ObjectDetector
import iancornelius.camerax_people_detector.ui.*

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ObjectViewModel>()

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
            CameraView().Show(ObjectDetector {viewModel.setObject(it) })
            ObjectBounds().ObjectBounds(viewModel.objectBoundingBox)
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