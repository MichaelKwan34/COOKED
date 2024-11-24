package com.group34.cooked

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

object SpinnerAdapter {

    // Creates a spinner with a given array and executes the setter function when an item is selected
    fun createDefaultSpinnerAdapter(
        context: Context,
        array: Array<String>,
        spinner: Spinner,
        setter: (String) -> Unit
    ) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, array)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setter(array[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}