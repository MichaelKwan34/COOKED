package com.group34.cooked.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.group34.cooked.DetailedRecipeActivity
import com.group34.cooked.NewRecipeActivity
import com.group34.cooked.R
import com.group34.cooked.RecipePreviewAdapter
import com.group34.cooked.RecipeViewModel


class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private lateinit var recipePreviewAdapter: RecipePreviewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: RecipeViewModel
    private lateinit var userId: String
    private lateinit var dividerItemDecoration: DividerItemDecoration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModel = RecipeViewModel()
        recyclerView = view.findViewById(R.id.recycler_view)
        recipePreviewAdapter = RecipePreviewAdapter(
            context = requireContext(),
            // this will launch the recipe details page as an activity
            onItemClick = { recipe ->
                //Log.d("RecipePreviewAdapter", "Recipe clicked: $recipe")
                val intent = Intent(requireContext(), DetailedRecipeActivity::class.java)
                intent.putExtra("recipe_id", recipe.id)
                startActivity(intent)
            })
        recyclerView.adapter = recipePreviewAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            1
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        viewModel.observeUserSavedRecipes(userId)
        viewModel.savedRecipes.observe(viewLifecycleOwner) { recipes ->
            recipePreviewAdapter.submitList(recipes)
        }
        val btnAddRecipe = view.findViewById<Button>(R.id.btn_add_recipe)
        btnAddRecipe.setOnClickListener {
            startActivity(Intent(context, NewRecipeActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // refresh when saved recipes are updated
        viewModel.savedRecipes.observe(viewLifecycleOwner) { recipes ->
            recipePreviewAdapter.submitList(recipes)
        }
    }
}