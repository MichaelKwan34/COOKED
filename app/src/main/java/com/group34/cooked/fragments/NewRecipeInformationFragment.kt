package com.group34.cooked.fragments

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage
import com.group34.cooked.NewRecipeActivity
import com.group34.cooked.NewRecipeViewModel
import com.group34.cooked.R
import com.group34.cooked.SpinnerAdapter
import com.group34.cooked.databinding.FragmentNewRecipeInformationBinding
import com.group34.cooked.models.Course
import com.group34.cooked.models.RecipeCreationStatus
import com.group34.cooked.models.RecipeDifficulty

class NewRecipeInformationFragment : Fragment(R.layout.fragment_new_recipe_information) {
    private var _binding: FragmentNewRecipeInformationBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnOcrUpload: Button
    private lateinit var photo: ImageView
    private lateinit var btnNewPhoto: TextView
    private lateinit var etName: EditText
    private lateinit var spCourse: Spinner
    private lateinit var spDifficulty: Spinner
    private lateinit var npDurationHour: NumberPicker
    private lateinit var npDurationMinute: NumberPicker
    private lateinit var tvDurationDescription: TextView
    private lateinit var etServings: EditText
    private lateinit var btnDraft: Button
    private lateinit var btnPublish: Button

    private lateinit var newRecipeViewModel: NewRecipeViewModel

    private lateinit var cameraLauncher: ActivityResultLauncher<Void?>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private lateinit var storage: FirebaseStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewRecipeInformationBinding.bind(view)

        // Initialize the views
        btnOcrUpload = binding.ocrRecipeUpload
        photo = binding.newRecipePhoto
        btnNewPhoto = binding.newRecipePhotoButton
        etName = binding.newRecipeName
        spCourse = binding.newRecipeCourse
        spDifficulty = binding.newRecipeDifficulty
        npDurationHour = binding.newRecipeDurationHours
        npDurationMinute = binding.newRecipeDurationMinutes
        tvDurationDescription = binding.newRecipeDurationDescription
        etServings = binding.newRecipeServings
        btnDraft = binding.newRecipeDraft
        btnPublish = binding.newRecipePublish

        // Shared view model with activity
        newRecipeViewModel = ViewModelProvider(requireActivity())[NewRecipeViewModel::class.java]

        // Initialize Firebase storage
        storage = FirebaseStorage.getInstance()

        // Use camera to take photo
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                photo.setImageBitmap(bitmap)
                newRecipeViewModel.setPhotoBitmap(bitmap)
            }
        }

        permissionLauncher = registerForActivityResult((ActivityResultContracts.RequestPermission())) { isGranted ->
            if (isGranted) {
                cameraLauncher.launch(null)
            } else {
                Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }

        initButtons()
        initDuration()
        initDifficultySpinner()
        initCourseSpinner()

        updateViewModel()
    }

    private fun initButtons() {
        btnOcrUpload.setOnClickListener {

        }

        btnNewPhoto.setOnClickListener {
            if (checkSelfPermission(requireContext(), CAMERA) == PERMISSION_GRANTED) {
                cameraLauncher.launch(null)
            } else {
                permissionLauncher.launch(CAMERA)
            }
        }

        btnDraft.setOnClickListener {
            save(RecipeCreationStatus.DRAFT)
        }

        btnPublish.setOnClickListener {
            save(RecipeCreationStatus.PUBLISHED)
        }
    }

    private fun initDuration() {
        npDurationHour.minValue = 0
        npDurationHour.maxValue = 24
        npDurationHour.wrapSelectorWheel = false

        npDurationMinute.minValue = 0
        npDurationMinute.maxValue = 59
        npDurationMinute.wrapSelectorWheel = false
    }

    // Initialize the difficulty spinner with item selection listener
    private fun initDifficultySpinner() {
        val difficulties = RecipeDifficulty.entries.map { it.toString() }.toTypedArray()
        SpinnerAdapter.createDefaultSpinnerAdapter(requireContext(), difficulties, spDifficulty) { difficulty ->
            newRecipeViewModel.setDifficulty(difficulty)
        }
    }

    // Initialize the course spinner with item selection listener
    private fun initCourseSpinner() {
        val courses = Course.entries.map { it.toString() }.toTypedArray()
        SpinnerAdapter.createDefaultSpinnerAdapter(requireContext(), courses, spCourse) { course ->
            newRecipeViewModel.setCourse(course)
        }
    }

    // Use listeners to update the view model
    private fun updateViewModel() {
        etName.addTextChangedListener {
            if (etName.text.trim().isNotEmpty()) {
                newRecipeViewModel.setName(etName.text.trim().toString())
            }
        }

        etServings.addTextChangedListener {
            if (etServings.text.trim().isNotEmpty()) {
                newRecipeViewModel.setServings(etServings.text.trim().toString().toInt())
            }
        }

        npDurationHour.setOnValueChangedListener { _, _, value ->
            newRecipeViewModel.setDurationHour(value)
        }

        npDurationMinute.setOnValueChangedListener { _, _, value ->
            newRecipeViewModel.setDurationMinute(value)
        }
    }

    // Validates the input fields
    // Returns true if valid. Otherwise false
    private fun areInputsValid(): Boolean {
        // Check edit text fields for empty values
        val etValues = listOf(etName, etServings)
        val isEtEmpty = etValues.stream()
            .filter {
                it.text?.isEmpty() ?: true
            }
            .map {
                it.hint = "Field is required"
                it.error = "Field is required"
            }
            .count() > 0

        // Check number picker fields for an overall time of 0
        var isTimeZero = false
        if (npDurationHour.value + npDurationMinute.value == 0) {
            tvDurationDescription.error = "Duration must be greater than 0"
            tvDurationDescription.setTextColor(resources.getColor(R.color.danger))
            isTimeZero = true
        } else {
            tvDurationDescription.error = null
            tvDurationDescription.setTextColor(resources.getColor(R.color.textForm))
        }

        return !isEtEmpty && !isTimeZero
    }

    // Save the recipe with the given status
    private fun save(status: RecipeCreationStatus) {
        if (status == RecipeCreationStatus.PUBLISHED && !areInputsValid()) {
            Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }
        newRecipeViewModel.setStatus(status.toString())

        // Save the recipe to Firestore
        newRecipeViewModel
            .saveRecipeToFireStore()
            .addOnSuccessListener { documentReference ->
                Log.d("NewRecipe", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("NewRecipe", "Error adding document", e)
            }

        (activity as? NewRecipeActivity)?.finish()
    }
}