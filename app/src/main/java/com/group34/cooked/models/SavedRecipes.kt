package com.group34.cooked.models

/*
Saved Recipes are the recipes that are displayed on the saved recipes page for users
 */
data class SavedRecipe(
    val recipeId: String = "",
    val userId: String = ""
)