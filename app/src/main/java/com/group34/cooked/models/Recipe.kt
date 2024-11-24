package com.group34.cooked.models

data class Recipe(
    var id : String = "",
    var creatorId: String = "",
    var photo: String = "", // URI
    var name: String = "",
    var course: String = "",
    var difficulty: String = "", // Easy, Medium, Hard
    var duration: Int = -1, // In minutes
    var servings: Int = -1,
    var status: String = "", // Draft, Published
    var averageStars: Double = 0.0, // 0 - 5
    var ingredients: MutableList<Ingredient> = mutableListOf(),
    var instructions: MutableList<Instruction> = mutableListOf(),
)
