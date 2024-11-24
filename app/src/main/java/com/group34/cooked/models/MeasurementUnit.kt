package com.group34.cooked.models

enum class MeasurementUnit(val label: String, val unit: String) {
    WHOLE("", ""),                      // Whole quantity
    GRAMS("Grams", "g"),
    KILOGRAMS("Kilograms", "kg"),
    CUPS("Cups", "C"),
    TABLESPOONS("Tablespoons", "tbps"),
    TEASPOONS("Teaspoons", "tsp"),
    LITRES("Litres", "L"),
    MILLILITRES("Millilitres", "mL");

    companion object {
        fun getFullName(unit: MeasurementUnit): String {
            if (unit == WHOLE) return ""
            return unit.label + " (" + unit.unit + ")"
        }
    }
}