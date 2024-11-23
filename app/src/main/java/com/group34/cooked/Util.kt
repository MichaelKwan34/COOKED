package com.group34.cooked

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.group34.cooked.models.Recipe

object Util {
    fun addTestRecipe(recipe: Recipe) {
        // adds a recipe, used for testing
        val db = FirebaseFirestore.getInstance()
        Log.d("Firestore", "Adding test recipe")
        db.collection("recipes")
            .add(recipe)
            .addOnSuccessListener { documentReference ->
                val generatedId = documentReference.id
                recipe.id = generatedId

                // Update the recipe in Firestore to include the generated ID
                db.collection("recipes").document(generatedId)
                    .update("id", generatedId) // Update only the `id` field
                    .addOnSuccessListener {
                        Log.d("Firestore", "Recipe ID field updated in Firestore")
                    }
                    .addOnFailureListener { exception ->
                        Log.d("Firestore", "Error updating recipe ID: ${exception.message}")
                    }
            }
            .addOnFailureListener { exception ->
                Log.d("Firestore", "Error adding test recipe: ${exception.message}")
            }
    }
}