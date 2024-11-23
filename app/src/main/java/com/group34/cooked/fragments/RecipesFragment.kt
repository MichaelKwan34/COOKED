package com.group34.cooked.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.group34.cooked.R
import com.group34.cooked.RecipeAdapter
import com.group34.cooked.SavedRecipesViewModel
import com.group34.cooked.Util
import com.group34.cooked.models.Ingredient
import com.group34.cooked.models.Instruction
import com.group34.cooked.models.Measurement
import com.group34.cooked.models.Recipe


class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: SavedRecipesViewModel
    private lateinit var userId: String
    private lateinit var dividerItemDecoration: DividerItemDecoration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModel = SavedRecipesViewModel()
        recyclerView = view.findViewById(R.id.recycler_view)
        recipeAdapter = RecipeAdapter(
            context = requireContext(),
            // this will launch the recipe details page
            onItemClick = { recipe ->})



        recyclerView.adapter = recipeAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            1
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        viewModel.observeUserSavedRecipes(userId)
        viewModel.savedRecipes.observe(viewLifecycleOwner) { recipes ->
            recipeAdapter.submitList(recipes)
        }
    }

    override fun onResume() {
        super.onResume()
        // refresh when saved recipes are updated
        viewModel.savedRecipes.observe(viewLifecycleOwner) { recipes ->
            recipeAdapter.submitList(recipes)
        }
    }


}