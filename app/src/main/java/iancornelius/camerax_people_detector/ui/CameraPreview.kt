package iancornelius.camerax_people_detector.ui

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.display.DisplayManager
import android.util.DisplayMetrics
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwitchCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import iancornelius.camerax_people_detector.algorithms.PeopleDetector
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class CameraPreview {
    private var cameraProvider: ProcessCameraProvider? = null
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var camera: Camera? = null
    private lateinit var displayManager: DisplayManager
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null


    @androidx.compose.ui.tooling.preview.Preview
    @Composable
    fun DisplayCamera() {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        lateinit var previewView: PreviewView

        Scaffold(topBar = { },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                            CameraSelector.LENS_FACING_BACK
                        } else {
                            CameraSelector.LENS_FACING_FRONT
                        }
                        bindCameraUseCases(previewView, lifecycleOwner, context)
                    },
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(Icons.Default.SwitchCamera,"")
                }
            }, content = {
                AndroidView(modifier = Modifier.fillMaxSize(),
                    factory = { _context ->
                    previewView = PreviewView(_context)
                    cameraProviderFuture.addListener({
                        cameraProvider = cameraProviderFuture.get()
                        lensFacing = when {
                            hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                            hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                            else -> throw IllegalStateException("Back and front camera are unavailable")
                        }
                        bindCameraUseCases(previewView, lifecycleOwner, context)
                    }, ContextCompat.getMainExecutor(context))
                    previewView
                })
            }
        )
    }

    @SuppressLint("RestrictedApi")
    private fun bindCameraUseCases(previewView: PreviewView, lifecycleOwner: LifecycleOwner, context: Context) {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = displayManager.displays[0].rotation
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
        val executor = ContextCompat.getMainExecutor(context)
        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setAnalyzer(executor, PeopleDetector { _ ->
//                    Log.d("PeopleDetector-Debug", "$faces")
                })
            }

        cameraProvider.unbindAll()
        camera = cameraProvider.bindToLifecycle(
            lifecycleOwner, cameraSelector, preview, imageAnalyzer)
        preview?.setSurfaceProvider(previewView.surfaceProvider)
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }
}