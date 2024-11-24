package com.group34.cooked.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.group34.cooked.ListAdapter
import com.group34.cooked.NewRecipeViewModel
import com.group34.cooked.R
import com.group34.cooked.databinding.FragmentNewRecipePublishBinding

class NewRecipePublishFragment : Fragment(R.layout.fragment_new_recipe_publish) {
    private var _binding: FragmentNewRecipePublishBinding? = null
    private val binding get() = _binding!!

    private lateinit var ivPhoto: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvServings: TextView
    private lateinit var lvIngredients: ListView
    private lateinit var lvInstructions: ListView

    private lateinit var newRecipeViewModel: NewRecipeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewRecipePublishBinding.bind(view)

        ivPhoto = binding.newRecipePublishPhoto
        tvTitle = binding.newRecipePublishTitle
        tvServings = binding.newRecipePublishServings
        lvIngredients = binding.newRecipePublishIngredients
        lvInstructions = binding.newRecipePublishInstructions

        // Shared view model with activity
        newRecipeViewModel = ViewModelProvider(requireActivity())[NewRecipeViewModel::class.java]

        // Observe photo changes
        newRecipeViewModel.photoBitmap.observe(viewLifecycleOwner) { photoBitmap ->
            ivPhoto.setImageBitmap(photoBitmap)
        }

        // Observe recipe changes
        newRecipeViewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            tvTitle.text = recipe.name
            tvServings.text =  context?.getString(R.string.serving_size_template, recipe.servings)
            lvIngredients.adapter = ListAdapter(requireContext(), recipe.ingredients, isPublish = true)
            lvInstructions.adapter = ListAdapter(requireContext(), recipe.instructions, isPublish = true)
        }
    }
}