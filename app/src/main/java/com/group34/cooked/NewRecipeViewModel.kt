package com.group34.cooked

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.group34.cooked.models.Ingredient
import com.group34.cooked.models.Instruction
import com.group34.cooked.models.Recipe
import com.group34.cooked.models.RecipeCreationStatus

class NewRecipeViewModel(
    private val imageRepository: ImageRepository = ImageRepository(),
    private val recipeRepository: RecipeRepository = RecipeRepository()
) : ViewModel() {
    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe> get() = _recipe

    private val _photoBitmap = MutableLiveData<Bitmap?>()
    val photoBitmap: LiveData<Bitmap?> get() = _photoBitmap

    init {
        _recipe.value = Recipe()
        _photoBitmap.value = null
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

        // Reorder the step numbers
        _recipe.value?.instructions?.forEachIndexed { index, newInstruction ->
            newInstruction.stepNumber = index + 1
        }
    }

    fun setAverageStars(averageStars: Double) {
        _recipe.value = _recipe.value?.copy(averageStars = averageStars)
    }

    fun setCreatorId(creatorId: String) {
        _recipe.value = _recipe.value?.copy(creatorId = creatorId)
    }

    fun setPhotoUri(url: String) {
        _recipe.value = _recipe.value?.copy(photo = url)
    }

    fun setPhotoBitmap(bitmap: Bitmap?) {
        _photoBitmap.value = bitmap
    }


    // Returns a task that adds the recipe to the firestore
    // Use the return to check if add is successful or not
    fun saveRecipeToFireStore(status: RecipeCreationStatus): Task<DocumentReference> {
        setStatus(status.toString())

        // Save recipe without image
        if (_photoBitmap.value == null) {
            return recipeRepository.saveRecipe(_recipe.value!!)
        }

        // Save recipe with image
        val uriTask = imageRepository.uploadImage(_photoBitmap.value)
        return uriTask.continueWithTask { task ->
            if (task.isSuccessful) {
                setPhotoUri(task.result.toString())
                recipeRepository.saveRecipe(_recipe.value!!)
            } else {
                Tasks.forException(Exception("Error saving recipe with image: ${task.exception}"))
            }
        }
    }
}