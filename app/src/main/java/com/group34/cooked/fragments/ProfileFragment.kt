package com.group34.cooked.fragments

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.group34.cooked.R
import com.group34.cooked.UserViewModel
import com.group34.cooked.UserRepository

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var profileImage: ShapeableImageView
    private lateinit var editNameEditText: TextInputEditText
    private lateinit var photoButton: MaterialButton
    private lateinit var saveButton: MaterialButton

    private lateinit var userViewModel: UserViewModel
    private lateinit var userId : String

    private lateinit var cameraLauncher: ActivityResultLauncher<Void?>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = UserViewModel(UserRepository())
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Initialize views
        profileImage = view.findViewById(R.id.profile_image)
        editNameEditText = view.findViewById(R.id.edit_name_editText)
        photoButton = view.findViewById(R.id.photo_button)
        saveButton = view.findViewById(R.id.save_button)

        // Use camera to take photo
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                profileImage.setImageBitmap(bitmap)
                userViewModel.setTempPhoto(bitmap)
            }
        }

        permissionLauncher = registerForActivityResult((ActivityResultContracts.RequestPermission())) { isGranted ->
            if (isGranted) {
                cameraLauncher.launch(null)
            } else {
                Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe LiveData from the ViewModel
        userViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                editNameEditText.setText(it.name)
                Glide.with(this).load(it.photo).into(profileImage)
            }
        })

        // Handle the photo button click to select a new photo
        photoButton.setOnClickListener {
            if (checkSelfPermission(requireContext(), CAMERA) == PERMISSION_GRANTED) {
                cameraLauncher.launch(null)
            } else {
                permissionLauncher.launch(CAMERA)
            }
        }

        saveButton.setOnClickListener {
            val newName = editNameEditText.text.toString()
            val newPhoto = userViewModel.tempPhoto.value

            if (newName.isNotBlank()) {
                userViewModel.saveChanges(userId, newName, newPhoto) { success ->
                    if (success) {
                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        userViewModel.fetchUser(userId)
    }
}