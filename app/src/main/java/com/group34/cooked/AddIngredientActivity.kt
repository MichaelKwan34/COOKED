package com.group34.cooked

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.group34.cooked.models.Ingredient
import com.group34.cooked.models.Measurement
import com.group34.cooked.models.MeasurementUnit

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
            val quantity = etQuantity.text.toString().toIntOrNull() ?: 0

            val measurementUnit = MeasurementUnit.entries.find { it.fullName == selectUnit }?: MeasurementUnit.WHOLE
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
    private fun areInputsValid(name: String, quantity: Int): Boolean {
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
        val measurementUnits = MeasurementUnit.entries.map { it.fullName }.toTypedArray()

        // Create an ArrayAdapter using the measurement array and a default spinner layout
        applicationContext?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                measurementUnits
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spUnit.adapter = adapter

                // Handle spinner item selection
                spUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectUnit = measurementUnits[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }
    }
}