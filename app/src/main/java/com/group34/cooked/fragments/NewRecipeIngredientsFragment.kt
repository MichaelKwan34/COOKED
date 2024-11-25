package com.group34.cooked.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.group34.cooked.AddIngredientActivity
import com.group34.cooked.ListAdapter
import com.group34.cooked.NewRecipeViewModel
import com.group34.cooked.R
import com.group34.cooked.databinding.FragmentNewRecipeIngredientsBinding
import com.group34.cooked.models.Ingredient

class NewRecipeIngredientsFragment : Fragment(R.layout.fragment_new_recipe_ingredients) {
    private var _binding: FragmentNewRecipeIngredientsBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnNewIngredient: TextView
    private lateinit var listIngredients: ListView

    private lateinit var newRecipeViewModel: NewRecipeViewModel

    private lateinit var addIngredientLauncher: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewRecipeIngredientsBinding.bind(view)

        btnNewIngredient = binding.newRecipeIngredientAdd
        listIngredients = binding.newRecipeIngredientList

        // Shared view model with activity
        newRecipeViewModel = ViewModelProvider(requireActivity())[NewRecipeViewModel::class.java]

        val ingredientListAdapter = ListAdapter(requireContext(), arrayListOf<Ingredient>(), newRecipeViewModel)
        listIngredients.adapter = ingredientListAdapter

        newRecipeViewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            ingredientListAdapter.setList(recipe.ingredients)
        }

        // Launch AddIngredientActivity
        btnNewIngredient.setOnClickListener {
            val intent = Intent(requireContext(), AddIngredientActivity::class.java)
            addIngredientLauncher.launch(intent)
        }

        // Register the launcher to handle the result
        addIngredientLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getParcelableExtra<Ingredient>("ingredient")?.let { ingredient ->
                    newRecipeViewModel.addIngredient(ingredient)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        addIngredientLauncher.unregister()
    }
}