package com.amitranofinzi.vimata.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.database.AppDatabase
import com.amitranofinzi.vimata.data.repository.CameraRepository
import com.amitranofinzi.vimata.ui.navigation.InitializableViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing camera permissions and video uploading.
 * It interacts with the CameraRepository for uploading videos and manages camera permission status.
 */
class CameraViewModel : ViewModel(), InitializableViewModel {
    lateinit var appDatabase: AppDatabase
    lateinit var context: Context

    /**
     * Initializes the ViewModel with the provided database and context.
     * This should be called before using the ViewModel.
     *
     * @param appDatabase The application database instance.
     * @param context The application context.
     */
    override fun initialize(appDatabase: AppDatabase, context: Context) {
        this.appDatabase = appDatabase
        this.context = context
    }
    private val cameraRepository: CameraRepository = CameraRepository()

    // Channel to communicate permission requests and results
    private val permissionRequestChannel = Channel<String>()
    val permissionRequest = permissionRequestChannel.receiveAsFlow()

    // State flow to track permission granted status
    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> get() = _permissionGranted

    // State to track if permission request was made
    private val _permissionRequested = MutableStateFlow(false)
    val permissionRequested: StateFlow<Boolean> get() = _permissionRequested


    /**
     * Requests camera permission by sending a permission request through the channel.
     */
    fun requestCameraPermission() {
        viewModelScope.launch {
            _permissionRequested.value = true
            permissionRequestChannel.send(android.Manifest.permission.CAMERA)
        }
    }

    /**
     * Updates the camera permission status.
     *
     * @param granted True if permission was granted, false otherwise.
     */
    fun updatePermissionStatus(granted: Boolean) {
        _permissionGranted.value = granted
    }

    /**
     * Checks if camera permission is granted and invokes the provided callback with the result.
     *
     * @param onResult Callback function to receive the permission status.
     */
    fun checkCameraPermission(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            onResult(_permissionGranted.value)
        }
    }

    /**
     * Uploads a video to Firebase using the provided video URI and test ID.
     *
     * @param context The application context.
     * @param videoUri The URI of the video to upload.
     * @param testID The ID associated with the test for which the video is uploaded.
     */
    fun uploadVideo(context: Context, videoUri: Uri, testID: String) {
        viewModelScope.launch {
            Log.d("CameraViewModel", "launching uploadVideo ${testID}")
            cameraRepository.uploadVideoToFirebase(context, videoUri, testID)
        }
    }
}
