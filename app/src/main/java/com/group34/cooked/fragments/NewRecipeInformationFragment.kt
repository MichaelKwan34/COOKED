package com.group34.cooked.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.group34.cooked.NewRecipeActivity
import com.group34.cooked.NewRecipeViewModel
import com.group34.cooked.R
import com.group34.cooked.databinding.FragmentNewRecipeInformationBinding
import com.group34.cooked.models.RecipeCreationStatus
import com.group34.cooked.models.RecipeDifficulty

class NewRecipeInformationFragment : Fragment(R.layout.fragment_new_recipe_information) {
    private var _binding: FragmentNewRecipeInformationBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnOcrUpload: Button
    private lateinit var photo: ImageView
    private lateinit var btnNewPhoto: TextView
    private lateinit var etName: EditText
    private lateinit var etCourse: EditText
    private lateinit var spDifficulty: Spinner
    private lateinit var npDurationHour: NumberPicker
    private lateinit var npDurationMinute: NumberPicker
    private lateinit var tvDurationDescription: TextView
    private lateinit var etServings: EditText
    private lateinit var btnDraft: Button
    private lateinit var btnPublish: Button

    private var selectDifficulty = RecipeDifficulty.EASY.id

    private lateinit var newRecipeViewModel: NewRecipeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewRecipeInformationBinding.bind(view)

        // Initialize the views
        btnOcrUpload = binding.ocrRecipeUpload
        photo = binding.newRecipePhoto
        btnNewPhoto = binding.newRecipePhotoButton
        etName = binding.newRecipeName
        etCourse = binding.newRecipeCourse
        spDifficulty = binding.newRecipeDifficulty
        npDurationHour = binding.newRecipeDurationHours
        npDurationMinute = binding.newRecipeDurationMinutes
        tvDurationDescription = binding.newRecipeDurationDescription
        etServings = binding.newRecipeServings
        btnDraft = binding.newRecipeDraft
        btnPublish = binding.newRecipePublish

        // Shared view model with activity
        newRecipeViewModel = ViewModelProvider(requireActivity())[NewRecipeViewModel::class.java]

        initButtons()
        initDuration()
        initDifficultySpinner()

        updateViewModel()
    }

    private fun initButtons() {
        btnOcrUpload.setOnClickListener {

        }

        btnNewPhoto.setOnClickListener {

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

    private fun initDifficultySpinner() {
        // Create an ArrayAdapter using the difficulty array and a default spinner layout
        activity?.applicationContext?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.difficulty_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spDifficulty.adapter = adapter

                // Handle spinner item selection
                spDifficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectDifficulty = position
                        newRecipeViewModel.setDifficulty(RecipeDifficulty.findById(selectDifficulty).value)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }
        }
    }

    // Use listeners to update the view model
    // Difficulty is updated by initDifficultySpinner
    private fun updateViewModel() {
        etName.addTextChangedListener {
            if (etName.text.trim().isNotEmpty()) {
                newRecipeViewModel.setName(etName.text.trim().toString())
            }
        }

        etCourse.addTextChangedListener {
            if (etCourse.text.trim().isNotEmpty()) {
                newRecipeViewModel.setCourse(etCourse.text.trim().toString())
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
    private fun validateInput(): Boolean {
        // Check edit text fields for empty values
        val etValues = listOf(
            etName,
            etCourse,
            etServings,
        )
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
        if (!validateInput()) {
            return
        }
        newRecipeViewModel.setStatus(status.value)

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