package com.group34.cooked

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddPantryListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry_add_item)

        val name : EditText = findViewById(R.id.name_pantry)
        val quantity : EditText = findViewById(R.id.quantity_pantry)
        val unit : EditText = findViewById(R.id.unit_pantry)

        val backButton : Button = findViewById(R.id.back_pantry_add_item)
        backButton.setOnClickListener{
            finish()
        }

        val saveButton : Button = findViewById(R.id.save_pantry)
        saveButton.setOnClickListener{
            if (validateInputs(name, quantity, unit)) {
                addItemToDatabase()
                finish()
            }
        }
    }

    private fun addItemToDatabase() {
        val db = FirebaseFirestore.getInstance()
        val pantryListRef = db.collection("pantryList")

        pantryListRef.limit(1).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Get the first document reference
                    val document = querySnapshot.documents[0]
                    val documentRef = pantryListRef.document(document.id)

                    // Retrieve the existing map of items or initialize a new one
                    val existingItems = document.get("items") as? Map<String, String> ?: mapOf()
                    val mutableItems = existingItems.toMutableMap()

                    // Calculate the next available key
                    val nextKey = (existingItems.keys.mapNotNull { it.toIntOrNull() }.maxOrNull() ?: 0) + 1

                    // Create the new item string
                    val itemString = "${findViewById<EditText>(R.id.quantity_pantry).text.toString().trim()} " +
                            "${findViewById<EditText>(R.id.unit_pantry).text.toString().trim()} " +
                            "${findViewById<EditText>(R.id.name_pantry).text.toString().trim()}"

                    // Add the new item to the map
                    mutableItems[nextKey.toString()] = itemString

                    // Update the Firestore document with the modified map
                    documentRef.update("items", mutableItems)
                        .addOnSuccessListener {
                            Log.d("pantryList", "Item successfully added to the existing list.")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("pantryList", "Error updating item: $exception")
                        }
                } else {
                    // No documents exist, so create a new one with the first item
                    val itemString = "${findViewById<EditText>(R.id.quantity_pantry).text.toString().trim()} " +
                            "${findViewById<EditText>(R.id.unit_pantry).text.toString().trim()} " +
                            "${findViewById<EditText>(R.id.name_pantry).text.toString().trim()}"

                    val newDocument = hashMapOf(
                        "items" to mapOf("1" to itemString)
                    )
                    pantryListRef.add(newDocument)
                        .addOnSuccessListener { documentReference ->
                            Log.d("pantryList", "New list created with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("pantryList", "Error creating new list: $exception")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("pantryList", "Error fetching documents: $exception")
            }
    }



    private fun validateInputs(name: EditText, quantity: EditText, unit: EditText): Boolean {
        var isValid = true

        // Validate Name
        if (name.text.toString().trim().isEmpty()) {
            name.error = "Name cannot be empty"
            isValid = false
        }

        // Validate Quantity
        val quantityText = quantity.text.toString().trim()
        if (quantityText.isEmpty()) {
            quantity.error = "Quantity cannot be empty"
            isValid = false
        } else {
            try {
                val quantityValue = quantityText.toInt()
                if (quantityValue <= 0) {
                    quantity.error = "Quantity must be a positive number"
                    isValid = false
                }
            } catch (e: NumberFormatException) {
                quantity.error = "Quantity must be a valid number"
                isValid = false
            }
        }

        // Validate Unit
        if (unit.text.toString().trim().isEmpty()) {
            unit.error = "Unit cannot be empty"
            isValid = false
        }

        return isValid
    }


}