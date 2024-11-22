package com.group34.cooked.models

enum class MeasurementUnit(val label: String, val unit: String, val fullName: String) {
    WHOLE("", "", ""),                      // Whole quantity
    GRAMS("Grams", "g", "Grams (g)"),
    KILOGRAMS("Kilograms", "kg", "Kilograms (kg)"),
    CUPS("Cups", "C", "Cups (C)"),
    TABLESPOONS("Tablespoons", "tbps", "Tablespoons (tbps)"),
    TEASPOONS("Teaspoons", "tsp", "Teaspoons (tsp)"),
    LITRES("Litres", "L", "Litres (L)"),
    MILLILITRES("Millilitres", "mL", "Millilitres (mL)"),
}