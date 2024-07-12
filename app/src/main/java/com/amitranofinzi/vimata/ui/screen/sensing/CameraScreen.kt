
package com.example.yourapp
import android.content.ContentValues
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.video.VideoRecordEvent.Finalize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.amitranofinzi.vimata.viewmodel.CameraViewModel

@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel = CameraViewModel(),
    testID: String,
    navController: NavController,
    ) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var videoCapture by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    var recording by remember { mutableStateOf<Recording?>(null) }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { androidx.camera.view.PreviewView(context) }

    LaunchedEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val recorder = Recorder.Builder().build()

        videoCapture = VideoCapture.withOutput(recorder)

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                videoCapture
            )
            Log.d("CameraScreen", "Camera bound to lifecycle")

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("CameraScreen", "Failed to bind camera use cases", e)

        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                recording?.let {
                    it.stop()
                    recording = null
                } ?: run {
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}.mp4")
                        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                        put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/YourAppName")
                    }

                    val mediaStoreOutputOptions = MediaStoreOutputOptions.Builder(
                        context.contentResolver,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    ).setContentValues(contentValues).build()

                    recording = videoCapture?.output?.prepareRecording(context, mediaStoreOutputOptions)
                        ?.start(ContextCompat.getMainExecutor(context)) { recordEvent ->
                            when (recordEvent) {
                                is VideoRecordEvent.Start -> {
                                    Toast.makeText(context, "Recording started", Toast.LENGTH_SHORT).show()
                                }
                                is Finalize -> {
                                    if (recordEvent.hasError()) {
                                        Log.e("CameraScreen", "Recording error: ${recordEvent.error}")
                                        Toast.makeText(context, "Recording failed", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Recording saved to: ${recordEvent.outputResults.outputUri}", Toast.LENGTH_SHORT).show()
                                        // Save the video URI to Firebase Storage
                                        val videoUri = recordEvent.outputResults.outputUri
                                        cameraViewModel.uploadVideo(context, videoUri, testID)
                                    }
                                }
                            }
                        }
                }
            }) {
                Text(if (recording != null) "STOP" else "REC")
            }
        }
    }

}


