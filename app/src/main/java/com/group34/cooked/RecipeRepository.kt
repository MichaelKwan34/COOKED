package com.group34.cooked

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.group34.cooked.models.Recipe

class RecipeRepository {

    private val db = FirebaseFirestore.getInstance()
    private val recipesCollection = db.collection("recipes")

    // Fetch all recipes
    fun getRecipes(): LiveData<List<Recipe>> {
        val liveData = MutableLiveData<List<Recipe>>()
        recipesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                liveData.value = emptyList() // Handle error
                return@addSnapshotListener
            }
            val recipes = snapshot?.toObjects(Recipe::class.java) ?: emptyList()
            liveData.value = recipes
        }
        return liveData
    }

    // Add or update a recipe
    fun saveRecipe(recipe: Recipe) {
        recipesCollection.document(recipe.id).set(recipe)
    }

    // Delete a recipe
    fun deleteRecipe(recipeId: String) {
        recipesCollection.document(recipeId).delete()
    }
}