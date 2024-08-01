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

class CameraViewModel : ViewModel(), InitializableViewModel {
    lateinit var appDatabase: AppDatabase
    lateinit var context: Context

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

    init {
        // Initial permission check or setup here if needed
    }

    fun requestCameraPermission() {
        viewModelScope.launch {
            _permissionRequested.value = true
            permissionRequestChannel.send(android.Manifest.permission.CAMERA)
        }
    }

    fun updatePermissionStatus(granted: Boolean) {
        _permissionGranted.value = granted
    }

    fun checkCameraPermission(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            onResult(_permissionGranted.value)
        }
    }

    fun uploadVideo(context: Context, videoUri: Uri, testID: String) {
        viewModelScope.launch {
            Log.d("CameraViewModel", "launching uploadVideo ${testID}")
            cameraRepository.uploadVideoToFirebase(context, videoUri, testID)
        }
    }
}
