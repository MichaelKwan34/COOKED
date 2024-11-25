package com.group34.cooked

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.group34.cooked.models.Ingredient
import com.group34.cooked.models.Instruction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.math.floor
import kotlin.math.round

class DetailedRecipeActivity : AppCompatActivity() {

    private lateinit var ingredientsRecyclerView: RecyclerView
    private lateinit var instructionsRecyclerView: RecyclerView
    private lateinit var ingredientsAdapter: RecipeAdapter<Ingredient>
    private lateinit var instructionsAdapter: RecipeAdapter<Instruction>
    private lateinit var currentRecipeViewModel: RecipeViewModel
    private lateinit var ingredientsDividerItemDecoration: DividerItemDecoration
    private lateinit var instructionsDividerItemDecoration: DividerItemDecoration

    private lateinit var backButton: ImageView
    private lateinit var editButton: ImageView

    private lateinit var recipeTitle: TextView
    private lateinit var shareButton: MaterialButton
    private lateinit var saveButton: MaterialButton
    private lateinit var creatorImage: ImageView
    private lateinit var creatorName: TextView
    private lateinit var servings: TextView
    private lateinit var rating: TextView
    private lateinit var recipeImage: ImageView
    private lateinit var recipeId: String
    private lateinit var headerRecipeName: TextView
    private lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_recipe) // Use your layout with two RecyclerViews

        recipeId = intent.getStringExtra("recipe_id").toString()
        // Log.d("DetailedRecipeActivity", "Recipe ID: $recipeId")

        currentUser = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        setupViews()
        setupOnClickListeners()

        // Initialize RecyclerViews
        ingredientsRecyclerView = findViewById(R.id.ingredients_recycler_view)
        instructionsRecyclerView = findViewById(R.id.instructions_recycler_view)

        // Set layout managers
        ingredientsRecyclerView.layoutManager = LinearLayoutManager(this)
        instructionsRecyclerView.layoutManager = LinearLayoutManager(this)

        ingredientsDividerItemDecoration = DividerItemDecoration(
            ingredientsRecyclerView.context,
            1
        )
        instructionsDividerItemDecoration = DividerItemDecoration(
            instructionsRecyclerView.context,
            1
        )
        ingredientsRecyclerView.addItemDecoration(ingredientsDividerItemDecoration)
        instructionsRecyclerView.addItemDecoration(instructionsDividerItemDecoration)

        // Initialize adapters
        ingredientsAdapter = RecipeAdapter(emptyList())
        instructionsAdapter = RecipeAdapter(emptyList())

        // Attach adapters to RecyclerViews
        ingredientsRecyclerView.adapter = ingredientsAdapter
        instructionsRecyclerView.adapter = instructionsAdapter

        // Set up ViewModel
        currentRecipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]
        currentRecipeViewModel.observeUserSavedRecipes(currentUser)

        recipeId.let {
            currentRecipeViewModel.getRecipeById(it)
        }

        // Observe the recipe data
        currentRecipeViewModel.currentRecipe.observe(this) { recipe ->
            recipe?.let {
                // Update adapters with ingredients and instructions
                ingredientsAdapter.setList(it.ingredients)
                instructionsAdapter.setList(it.instructions)
                loadRecipeData()
            }
        }

        // Switch between 'saved' and 'save'
        currentRecipeViewModel.savedRecipes.observe(this) { savedRecipes ->
            Log.d("DetailedRecipeActivity", "Saved Recipes: $savedRecipes")
            Log.d("DetailedRecipeActivity", "Current Recipe ID: $recipeId")
            val isSaved = savedRecipes.any { it.id == recipeId }
            if (isSaved) {
                saveButton.text = "Saved"
                saveButton.setIconResource(R.drawable.bookmark_filled)
            } else {
                saveButton.text = "Save"
                saveButton.setIconResource(R.drawable.bookmark_outline)
            }
        }


    }

    private fun setupViews() {
        recipeTitle = findViewById(R.id.recipeName)
        shareButton = findViewById(R.id.share_icon_button)
        saveButton = findViewById(R.id.save_icon_button)
        creatorImage = findViewById(R.id.creatorImage)
        creatorName = findViewById(R.id.creatorName)
        servings = findViewById(R.id.servings)
        rating = findViewById(R.id.rating)
        recipeImage = findViewById(R.id.recipe_image)
        headerRecipeName = findViewById(R.id.header_recipe_name)
        backButton = findViewById(R.id.back_button)
        editButton = findViewById(R.id.edit_button)
    }

    private fun setupOnClickListeners() {
        shareButton.setOnClickListener {
            // Handle share button click
        }

        saveButton.setOnClickListener {
            Log.d("DetailedRecipeActivity", "Save button clicked")
            val currentRecipeId = recipeId
            if (currentRecipeId.isNotEmpty()) {
                currentRecipeViewModel.savedRecipes.value?.let { savedRecipes ->
                    val isSaved = savedRecipes.any { it.id == currentRecipeId }
                    if (isSaved) {
                        currentRecipeViewModel.unsaveRecipe(currentUser, currentRecipeId)
                    } else {
                        currentRecipeViewModel.saveRecipe(currentUser, currentRecipeId)
                    }
                }
            }
        }

        backButton.setOnClickListener {
            finish()
        }
        editButton.setOnClickListener {
            // Handle edit button click
        }
    }

        private fun loadRecipeData() {
            val title = currentRecipeViewModel.currentRecipe.value?.name
            headerRecipeName.text = title
            recipeTitle.text = title
            // have this deal with fetching the user + name later
            val creatorId = currentRecipeViewModel.currentRecipe.value?.creatorId
            Log.d("DetailedRecipeActivity", "Creator ID: $creatorId")
            if (creatorId != "" && creatorId != null) {
                fetchCreatorData(creatorId)
            }
            servings.text = "Serves ${currentRecipeViewModel.currentRecipe.value?.servings ?: 0}"

            // I KNOW this is really scuffed, I'm just doing this for the demo
            val stars = currentRecipeViewModel.currentRecipe.value?.averageStars?.let { round(it) }
            if (stars?.toInt() == 1) {
                rating.text = "${currentRecipeViewModel.currentRecipe.value?.averageStars} ★"
            } else if (stars?.toInt() == 2) {
                rating.text = "${currentRecipeViewModel.currentRecipe.value?.averageStars} ★★"
            }
            else if (stars?.toInt() == 3) {
                rating.text = "${currentRecipeViewModel.currentRecipe.value?.averageStars} ★★★"
            }
            else if (stars?.toInt() == 4) {
                rating.text = "${currentRecipeViewModel.currentRecipe.value?.averageStars} ★★★★"
            }
            else if (stars?.toInt() == 5) {
                rating.text = "${currentRecipeViewModel.currentRecipe.value?.averageStars} ★★★★★"
            }

            // set image
            val imageUri = currentRecipeViewModel.currentRecipe.value?.photo
            // Log.d("DetailedRecipeActivity", "Image URI: $imageUri")
            if (imageUri != null && imageUri.isNotEmpty()) {
                Glide.with(this)
                    .load(imageUri) // Load the image from URI
                    .placeholder(R.drawable.outline_photo_camera_black_24) // Default image while loading
                    .error(R.drawable.outline_photo_camera_black_24) // Image if loading fails
                    .into(recipeImage) // Target ImageView
            } else {
                recipeImage.setImageResource(R.drawable.outline_photo_camera_black_24)
            }

            // remove edit button if they aren't the creator
            if (currentRecipeViewModel.currentRecipe.value?.creatorId != currentUser) {
                editButton.visibility = View.GONE
            }
            currentRecipeViewModel.observeUserSavedRecipes(currentUser)
        }


    private fun fetchCreatorData(creatorId: String) {
        Log.d("DetailedRecipeActivity", "Fetching creator data for ID: $creatorId")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRef = FirebaseFirestore.getInstance().collection("users").document(creatorId)
                val document = userRef.get().await() // Use Kotlin coroutines for async tasks
                withContext(Dispatchers.Main) {
                    if (document.exists()) {
                        val creatorNameValue = document.getString("name") ?: "N/A"
                        val creatorPhotoUrl = document.getString("photo")
                        creatorName.text = creatorNameValue
                        if (!creatorPhotoUrl.isNullOrEmpty()) {
                            Glide.with(this@DetailedRecipeActivity)
                                .load(creatorPhotoUrl) // Load the profile picture
                                .placeholder(R.drawable.outline_photo_camera_white_24)
                                .error(R.drawable.outline_photo_camera_white_24)
                                .into(creatorImage)
                        } else {
                            creatorImage.setImageResource(R.drawable.outline_photo_camera_white_24)
                        }
                    } else {
                        Log.e("DetailedRecipeActivity", "User document does not exist")
                    }
                }
            } catch (e: Exception) {
                Log.e("DetailedRecipeActivity", "Error fetching creator data: ${e.message}")
            }
        }
    }

}

