package com.group34.cooked

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Tasks
import com.group34.cooked.models.User

class UserViewModel(private val userRepository: UserRepository, private val imageRepository: ImageRepository = ImageRepository(),) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _updateStatus = MutableLiveData<Boolean>()
    val updateStatus: LiveData<Boolean> get() = _updateStatus

    private val _uploadStatus = MutableLiveData<String?>()
    val uploadStatus: LiveData<String?> get() = _uploadStatus

    private val _tempName = MutableLiveData<String>()
    val tempName: LiveData<String> get() = _tempName

    private val _tempPhoto = MutableLiveData<Bitmap?>()
    val tempPhoto: LiveData<Bitmap?> get() = _tempPhoto



    fun setTempPhoto(bitmap: Bitmap?) {
        _tempPhoto.value = bitmap
    }

    fun setTempName(name: String) {
        _tempName.value = name
    }

    // Fetch user details by userId
    fun fetchUser(userId: String) {
        userRepository.getUserById(userId) { user ->
            _user.value = user
        }
    }

    fun saveChanges(userId: String, newName: String, newPhoto: Bitmap?, callback: (Boolean) -> Unit) {
        if (newPhoto != null) {
            // Upload the new photo
            imageRepository.uploadImage(newPhoto).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val photoUrl = task.result
                    // Now update both the photo URL and the name in the user repository
                    Log.d("UserViewModel", "Saving changes with photo")
                    userRepository.updateUserProfile(userId, newName, photoUrl.toString()) { success ->
                        callback(success)
                    }
                } else {
                    callback(false)
                }
            }
        } else {
            // If no photo, just update the name
            Log.d("UserViewModel", "Saving changes with no photo")
            userRepository.updateUserProfile(userId, newName, null) { success ->
                callback(success)
            }
        }
    }
}