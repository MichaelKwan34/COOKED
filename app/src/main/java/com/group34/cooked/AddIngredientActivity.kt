package com.group34.cooked

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.group34.cooked.SpinnerAdapter.createDefaultSpinnerAdapter
import com.group34.cooked.models.Ingredient
import com.group34.cooked.models.Measurement
import com.group34.cooked.models.MeasurementUnit
import com.group34.cooked.models.MeasurementUnit.Companion.getFullName

class AddIngredientActivity : AppCompatActivity(R.layout.activity_add_ingredient)  {

    private lateinit var etName: EditText
    private lateinit var etQuantity: EditText
    private lateinit var spUnit: Spinner
    private lateinit var btnSave: Button

    private var selectUnit = MeasurementUnit.WHOLE.label

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        etName = findViewById(R.id.add_ingredient_name)
        etQuantity = findViewById(R.id.add_ingredient_quantity)
        spUnit = findViewById(R.id.add_ingredient_unit)
        btnSave = findViewById(R.id.save_ingredient)

        initMeasurementSpinner()

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val quantity = etQuantity.text.toString().toDoubleOrNull() ?: 0.0

            val measurementUnit = MeasurementUnit.entries.find { getFullName(it) == selectUnit }?: MeasurementUnit.WHOLE
            val measureName = measurementUnit.label
            val measureUnit = measurementUnit.unit
            val unit = Measurement(measureName, measureUnit)

            if (areInputsValid(name, quantity)) {
                val ingredient = Ingredient(name, quantity, unit)
                val intent = Intent()
                intent.putExtra("ingredient", ingredient)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    // Check if the input fields are valid
    private fun areInputsValid(name: String, quantity: Double): Boolean {
        val isNameBlank = if (name.isBlank()) {
            etName.error = "Name is required"
            true
        } else {
            false
        }

        val isQuantityZeroOrLess = if (quantity <= 0) {
            etQuantity.error = "Quantity must be greater than 0"
            true
        } else {
            false
        }

        return !isNameBlank && !isQuantityZeroOrLess
    }

    private fun initMeasurementSpinner() {
        val measurementUnits = MeasurementUnit.entries.map { getFullName(it) }.toTypedArray()
        createDefaultSpinnerAdapter(this, measurementUnits, spUnit) { unit ->
            selectUnit = unit
        }
    }
}