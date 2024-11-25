package com.group34.cooked.models

data class Rating(
    val userId: String = "",
    val recipeId: String = "",
    val stars: Int = 0,
)
