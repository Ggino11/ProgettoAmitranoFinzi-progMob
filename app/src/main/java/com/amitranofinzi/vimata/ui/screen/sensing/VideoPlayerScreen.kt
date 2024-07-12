package com.amitranofinzi.vimata.ui.screen.sensing

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoPlayerScreen(videoUrl: String) {
    val context = LocalContext.current
    var videoView by remember { mutableStateOf<VideoView?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                VideoView(context).apply {
                    setVideoURI(Uri.parse(videoUrl))
                    setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.start()
                        isPlaying = true
                    }
                    setOnCompletionListener { mediaPlayer ->
                        mediaPlayer.stop()
                        isPlaying = false
                    }
                    val mediaController = MediaController(context)
                    mediaController.setAnchorView(this)
                    setMediaController(mediaController)
                }
            },
            update = { view ->
                videoView = view
                view.setVideoURI(Uri.parse(videoUrl))
                view.requestFocus()
                view.start()
            },
            modifier = Modifier.weight(1f)
        )

    }
}