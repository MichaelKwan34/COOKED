package com.group34.cooked.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.group34.cooked.CurrentRecipeViewModel
import com.group34.cooked.ListAdapter
import com.group34.cooked.R
import com.group34.cooked.RecipeAdapter
import com.group34.cooked.RecipeViewModel
import com.group34.cooked.databinding.FragmentNewRecipeIngredientsBinding
import com.group34.cooked.models.Ingredient
import com.group34.cooked.models.Instruction

class DetailedRecipeFragment : Fragment(R.layout.fragment_detailed_recipe) {

    private lateinit var listIngredients: ListView
    private lateinit var listInstructions: ListView

    private lateinit var currentRecipeViewModel: RecipeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getString("recipe_id")
        Log.d("DetailedRecipeFragment", "Recipe ID: $recipeId")



        listIngredients = view.findViewById(R.id.ingredients_list)
        listInstructions = view.findViewById(R.id.instructions_list)

        // Shared view model with activity
        currentRecipeViewModel = ViewModelProvider(requireActivity())[RecipeViewModel::class.java]

        recipeId?.let {
            currentRecipeViewModel.getRecipeById(it)
        }

        val ingredientListAdapter =
            RecipeAdapter(requireContext(), arrayListOf<Ingredient>(), currentRecipeViewModel)
        listIngredients.adapter = ingredientListAdapter

        val instructionListAdapter = RecipeAdapter(requireContext(), arrayListOf<Instruction>(), currentRecipeViewModel)
        listInstructions.adapter = instructionListAdapter

        currentRecipeViewModel.currentRecipe.observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                ingredientListAdapter.setList(recipe.ingredients)
            }
            if (recipe != null) {
                instructionListAdapter.setList(recipe.instructions)
            }
        }
    }
}