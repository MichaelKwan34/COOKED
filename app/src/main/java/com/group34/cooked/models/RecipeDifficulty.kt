package com.group34.cooked.models

enum class RecipeDifficulty(val id: Int, val value: String) {
    EASY(0, "Easy"),
    MEDIUM(1, "Medium"),
    HARD(2, "Hard");

    companion object {
        fun findById(id: Int): RecipeDifficulty {
            if (id < 0 || id >= entries.size) {
                throw IllegalArgumentException("Invalid id")
            }
            return entries.first { it.id == id }
        }
    }
}

