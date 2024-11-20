package com.group34.cooked

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.group34.cooked.models.Ingredient
import com.group34.cooked.models.Instruction
import com.group34.cooked.models.Recipe

class NewRecipeViewModel : ViewModel() {
    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe> get() = _recipe

    init {
        _recipe.value = Recipe(name = "", course = "", difficulty = "", duration = 0, servings = 0, status = "")
    }

    fun setName(name: String) {
        _recipe.value = _recipe.value?.copy(name = name)
    }

    fun setCourse(course: String) {
        _recipe.value = _recipe.value?.copy(course = course)
    }

    fun setDifficulty(difficulty: String) {
        _recipe.value = _recipe.value?.copy(difficulty = difficulty)
    }

    fun setDurationHour(hours: Int) {
        val newDuration = hours * 60 + recipe.value!!.duration % 60
        _recipe.value = _recipe.value?.copy(duration = newDuration)
    }

    fun setDurationMinute(minutes: Int) {
        val newDuration = recipe.value!!.duration + minutes
        _recipe.value = _recipe.value?.copy(duration = newDuration)
    }

    fun setDuration(duration: Int) {
        _recipe.value = _recipe.value?.copy(duration = duration)
    }

    fun setServings(servings: Int) {
        _recipe.value = _recipe.value?.copy(servings = servings)
    }

    fun setStatus(status: String) {
        _recipe.value = _recipe.value?.copy(status = status)
    }

    fun addIngredient(ingredient: Ingredient) {
        _recipe.value = _recipe.value?.apply {
            ingredients.add(ingredient)
        }
    }

    fun removeIngredient(ingredient: Ingredient) {
        _recipe.value = _recipe.value?.apply {
            ingredients.remove(ingredient)
        }
    }

    fun addInstruction(instruction: Instruction) {
        _recipe.value = _recipe.value?.apply {
            instructions.add(instruction)
        }
    }

    fun removeInstruction(instruction: Instruction) {
        _recipe.value = _recipe.value?.apply {
            instructions.remove(instruction)
        }
    }

    fun setAverageStars(averageStars: Double) {
        _recipe.value = _recipe.value?.copy(averageStars = averageStars)
    }

    fun setCreatorId(creatorId: String) {
        _recipe.value = _recipe.value?.copy(creatorId = creatorId)
    }

    fun setPhotoUrl(url: String) {
        _recipe.value = _recipe.value?.copy(photo = url)
    }

    // Returns a task that adds the recipe to the firestore
    // Use the return to check if add is successful or not
    fun saveRecipeToFireStore(): Task<DocumentReference> {
        // Get the reference once added
        val recipeRef = Firebase.firestore
            .collection("recipes")
            .add(_recipe.value!!)


        // Add ingredients and instructions if the recipe is added
        recipeRef.addOnSuccessListener { documentReference ->
            val ingredientsCollectionRef = documentReference.collection("ingredients")
            _recipe.value!!.ingredients.forEach { ingredient ->
                ingredientsCollectionRef.add(ingredient)
            }

            val instructionsCollectionRef = documentReference.collection("instructions")
            _recipe.value!!.instructions.forEach { instruction ->
                instructionsCollectionRef.add(instruction)
            }
        }

        return recipeRef
    }
}