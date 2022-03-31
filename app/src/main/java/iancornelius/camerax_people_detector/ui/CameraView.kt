package iancornelius.camerax_people_detector.ui

import android.content.Context
import android.hardware.display.DisplayManager
import android.util.DisplayMetrics
import android.util.Size
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
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
import java.util.concurrent.Executor
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CameraView {

    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var displayManager: DisplayManager

    @Composable
    fun Show(imageAnalyser: ImageAnalysis.Analyzer) {
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
                        bindCameraView(lifecycleOwner, previewView, imageAnalyser, context)
                    },
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(Icons.Default.SwitchCamera,"")
                }
            }, content = {
                AndroidView(modifier = Modifier
                    .fillMaxSize(),
                    factory = { _context ->
                        previewView = PreviewView(_context)
                        cameraProviderFuture.addListener({
                            cameraProvider = cameraProviderFuture.get()
                            lensFacing = when {
                                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                                else -> throw IllegalStateException("Back and front camera are unavailable")
                            }
                            bindCameraView(lifecycleOwner, previewView, imageAnalyser, _context)
                        }, ContextCompat.getMainExecutor(context))
                        previewView
                    })
            }
        )
    }

    private fun bindCameraView(lifecycleOwner: LifecycleOwner, previewView: PreviewView,
                               imageAnalyser: ImageAnalysis.Analyzer, context: Context) {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = displayManager.displays[0].rotation
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")
        val preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
            .also { it.setSurfaceProvider(previewView.surfaceProvider) }
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector,
            setupImageAnalysis(previewView, ContextCompat.getMainExecutor(context), imageAnalyser),
            preview)

    }

    private fun setupImageAnalysis(previewView: PreviewView, executor: Executor,
                                   imageAnalyser: ImageAnalysis.Analyzer): ImageAnalysis {
        return ImageAnalysis.Builder()
            .setTargetResolution(Size(previewView.width, previewView.height))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply { setAnalyzer(executor, imageAnalyser)}
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