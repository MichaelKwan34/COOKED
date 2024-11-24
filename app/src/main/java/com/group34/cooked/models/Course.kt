package com.group34.cooked.models

enum class Course(val value: String) {
    APPETIZER("Appetizer"),
    MAIN("Main"),
    DESSERT("Dessert"),
    DRINK("Drink");

    override fun toString(): String {
        return value
    }
}