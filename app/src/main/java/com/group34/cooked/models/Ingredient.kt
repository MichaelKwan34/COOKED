package com.group34.cooked.models

data class Ingredient(
    var name: String,
    var quantity: Int,
    var measurement: String? = null // Grams, Liters, Cups, etc.
)
