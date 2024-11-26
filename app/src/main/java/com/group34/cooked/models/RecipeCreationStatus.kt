package com.group34.cooked.models

enum class RecipeCreationStatus(val value: String) {
    DRAFT("Draft"),
    PUBLISHED("Published");

    override fun toString(): String {
        return value
    }
}