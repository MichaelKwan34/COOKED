package com.group34.cooked

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class ImageRepository {

    // Upload image to Firebase Storage and return the download URL on success
    // Otherwise, return the exception on failure
    fun uploadImage(bitmap: Bitmap?, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        if (bitmap == null) {
            onFailure(Exception("Bitmap is null"))
            return
        }

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        val creatorId = FirebaseAuth.getInstance().currentUser?.uid ?: "unknown"
        val timestamp = System.currentTimeMillis()
        val fileName = "images/${creatorId}/${timestamp}.jpg"
        val storageRef = FirebaseStorage.getInstance().reference.child(fileName)

        // Upload image
        storageRef.putBytes(imageData)
            .addOnSuccessListener {
                storageRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        onSuccess(uri.toString())
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                    }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}