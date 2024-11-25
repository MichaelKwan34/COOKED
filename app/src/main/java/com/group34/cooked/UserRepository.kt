package com.group34.cooked

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.group34.cooked.models.User

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Fetch a user by ID
    fun getUserById(userId: String, callback: (User?) -> Unit) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = User(
                        id = userId,
                        name = document.getString("name") ?: "",
                        photo = document.getString("photo") ?: ""
                    )
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun updateUserProfile(userId: String, newName: String, newPhotoUrl: String?, callback: (Boolean) -> Unit) {
        val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

        val updates = hashMapOf<String, Any>(
            "name" to newName
        )

        newPhotoUrl?.let {
            updates["photo"] = newPhotoUrl
        }

        Log.d("UserRepository", "Updates: $updates")
        userRef.set(updates, SetOptions.merge())
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileUpdate", "Error updating profile: ${exception.message}")
                callback(false)
            }
    }
}