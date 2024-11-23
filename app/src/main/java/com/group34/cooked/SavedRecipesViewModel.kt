package com.group34.cooked

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.group34.cooked.models.Recipe

class SavedRecipesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _savedRecipes = MutableLiveData<List<Recipe>>()
    val savedRecipes: LiveData<List<Recipe>> = _savedRecipes

    private var savedRecipesListener: ListenerRegistration? = null

    fun observeUserSavedRecipes(userId: String) {
        Log.d("Firebase", "Observing user's saved recipes")
        // real-time listener to the savedRecipes document for the userId
        savedRecipesListener = db.collection("savedrecipes")
            .document(userId)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    Log.e("Firebase", "Error listening to saved recipes", error)
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Get the list of recipeIds
                    val recipeIds = documentSnapshot.get("recipeIds") as? List<String> ?: emptyList()
                    if (recipeIds.isNotEmpty()) {
                        // Fetch recipes using the recipeIds
                        db.collection("recipes")
                            .whereIn(FieldPath.documentId(), recipeIds)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                val recipes = querySnapshot.toObjects(Recipe::class.java)
                                _savedRecipes.value = recipes
                                Log.d("Firebase", "Saved recipes fetched: $recipes")
                            }
                            .addOnFailureListener { exception ->
                                Log.e("Firebase", "Error fetching recipes: ${exception.message}")
                            }
                    } else {
                        _savedRecipes.value = emptyList() // No saved recipes
                        Log.d("Firebase", "No saved recipes found")
                    }
                }
            }
    }

    fun saveRecipe(userId: String, recipeId: String) {
        db.collection("savedRecipes").document(userId).update("recipeIds", FieldValue.arrayUnion(recipeId))
    }
    fun unsaveRecipe(userId: String, recipeId: String) {
        db.collection("savedRecipes").document(userId).update("recipeIds", FieldValue.arrayRemove(recipeId))

    }
}