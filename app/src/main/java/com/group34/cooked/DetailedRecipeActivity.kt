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
import com.group34.cooked.models.Ingredient
import com.group34.cooked.models.Instruction

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
    private lateinit var shareButton: Button
    private lateinit var saveButton: Button
    private lateinit var creatorImage: ImageView
    private lateinit var creatorName: TextView
    private lateinit var servings: TextView
    private lateinit var rating: TextView
    private lateinit var recipeImage: ImageView
    private lateinit var recipeId: String
    private lateinit var headerRecipeName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_recipe) // Use your layout with two RecyclerViews

        recipeId = intent.getStringExtra("recipe_id").toString()
        // Log.d("DetailedRecipeActivity", "Recipe ID: $recipeId")

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
            // Handle save button click
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
            // creatorName.text = currentRecipeViewModel.currentRecipe.value?.creatorName ?: "N/A"
            servings.text = "Serves ${currentRecipeViewModel.currentRecipe.value?.servings ?: 0}"
            rating.text =
                (currentRecipeViewModel.currentRecipe.value?.averageStars ?: "4 ★★★★★").toString()
            // set image



        }
}

