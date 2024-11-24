package com.group34.cooked

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
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
    fun saveRecipe(recipe: Recipe): Task<DocumentReference> {
        val recipeRef = recipesCollection.add(recipe)

        recipeRef
            .addOnSuccessListener { documentReference ->
                // Add ingredients and instructions to subcollections
                val ingredientsCollectionRef = documentReference.collection("ingredients")
                recipe.ingredients.forEach { ingredient ->
                    ingredientsCollectionRef.add(ingredient)
                }

                val instructionsCollectionRef = documentReference.collection("instructions")
                recipe.instructions.forEach { instruction ->
                    instructionsCollectionRef.add(instruction)
                }
            }

        return recipeRef
    }

    // Delete a recipe
    fun deleteRecipe(recipeId: String) {
        recipesCollection.document(recipeId).delete()
    }
}