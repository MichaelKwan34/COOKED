package com.group34.cooked.models

enum class RecipeDifficulty(val id: Int, val value: String) {
    EASY(0, "Easy"),
    MEDIUM(1, "Medium"),
    HARD(2, "Hard");

    override fun toString(): String {
        return value
    }
}

