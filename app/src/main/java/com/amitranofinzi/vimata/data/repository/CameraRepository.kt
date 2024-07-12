package com.amitranofinzi.vimata.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class CameraRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    suspend fun uploadVideoToFirebase(context: Context, videoUri: Uri, testID: String) {
        val storageRef = storage.reference
        val videoRef = storageRef.child("videos/${videoUri.lastPathSegment}")
        val uploadTask = videoRef.putFile(videoUri)

        Log.d("CameraRepository", "Starting upload for URI: $videoUri")

        uploadTask.addOnSuccessListener {
            Log.d("CameraRepository", "Video uploaded successfully: ${videoUri.lastPathSegment}")

            videoRef.downloadUrl.addOnSuccessListener { uri ->
                Log.d("CameraRepository", "Download URL retrieved: $uri")
                updateTestVideoUrl(testID, uri.toString())
                Toast.makeText(context, "Upload successful!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { exception ->
                Log.e("CameraRepository", "Failed to get download URL", exception)
                Toast.makeText(context, "Failed to get download URL: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Log.e("CameraRepository", "Upload failed", exception)
            Toast.makeText(context, "Upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTestVideoUrl(testID: String, videoUrl: String) {
        val testRef = firestore.collection("tests").document(testID)
        testRef.update("videoUrl", videoUrl)
            .addOnSuccessListener {
                Log.d("CameraRepository", "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w("CameraRepository", "Error updating document", e)
            }
    }


}