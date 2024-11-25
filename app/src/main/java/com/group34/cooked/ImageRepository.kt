package com.group34.cooked

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class ImageRepository {

    // Upload image to Firebase Storage and return a task that contains the URI of the image
    // Otherwise, return an exception if the task fails
    fun uploadImage(
        bitmap: Bitmap?
    ): Task<Uri> {
        if (bitmap == null) {
            return Tasks.forException(Exception("Bitmap is null"))
        }

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        val creatorId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
        val timestamp = System.currentTimeMillis()
        val fileName = "images/${creatorId}/${timestamp}.jpg"
        val storageRef = FirebaseStorage.getInstance().reference.child(fileName)

        // Upload image and return the download URL
        return storageRef.putBytes(imageData)
            .continueWithTask { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl
                } else {
                    Tasks.forException(task.exception
                        ?: Exception("Unknown exception found while uploading image"))
                }
            }
    }
}