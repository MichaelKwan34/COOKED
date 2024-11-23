package com.group34.cooked

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.group34.cooked.models.Recipe

class CreatedRecipesViewModel(private val repository: RecipeRepository) : ViewModel() {

    fun getCreatedRecipes(userId: String): LiveData<List<Recipe>> {
        val liveData = MutableLiveData<List<Recipe>>()
        val db = FirebaseFirestore.getInstance()

        db.collection("recipes")
            .whereEqualTo("creatorId", userId) // Filter by the current user's ID
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recipes = querySnapshot.toObjects(Recipe::class.java)
                liveData.value = recipes
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error fetching created recipes", exception)
            }

        return liveData
    }
}