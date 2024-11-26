package com.group34.cooked

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddShoppingListActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list_add_item)

        val name : EditText = findViewById(R.id.name_shopping_list)
        val quantity : EditText = findViewById(R.id.quantity_shopping_list)
        val unit : EditText = findViewById(R.id.unit_shopping_list)

        val backButton : Button = findViewById(R.id.back_shopping_list_add_item)
        backButton.setOnClickListener{
            finish()
        }

        val saveButton : Button = findViewById(R.id.save_shopping_list)
        saveButton.setOnClickListener{
            if (validateInputs(name, quantity, unit)) {
                addItemToDatabase()
                finish()
            }
        }

    }

    private fun addItemToDatabase() {
        val db = FirebaseFirestore.getInstance()
        val shoppingListRef = db.collection("shoppingList")

        shoppingListRef.limit(1).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Get the first document reference
                    val document = querySnapshot.documents[0]
                    val documentRef = shoppingListRef.document(document.id)

                    // Retrieve the existing map of items or initialize a new one
                    val existingItems = document.get("items") as? Map<String, String> ?: mapOf()
                    val mutableItems = existingItems.toMutableMap()

                    // Calculate the next available key
                    val nextKey = (existingItems.keys.mapNotNull { it.toIntOrNull() }.maxOrNull() ?: 0) + 1

                    // Create the new item string
                    val itemString = "${findViewById<EditText>(R.id.quantity_shopping_list).text.toString().trim()} " +
                            "${findViewById<EditText>(R.id.unit_shopping_list).text.toString().trim()} " +
                            "${findViewById<EditText>(R.id.name_shopping_list).text.toString().trim()}"

                    // Add the new item to the map
                    mutableItems[nextKey.toString()] = itemString

                    // Update the Firestore document with the modified map
                    documentRef.update("items", mutableItems)
                        .addOnSuccessListener {
                            homeViewModel.fetchShoppingList()
                            Log.d("shoppingList", "Item successfully added to the existing list.")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("shoppingList", "Error updating item: $exception")
                        }
                } else {
                    // No documents exist, so create a new one with the first item
                    val itemString = "${findViewById<EditText>(R.id.quantity_shopping_list).text.toString().trim()} " +
                            "${findViewById<EditText>(R.id.unit_shopping_list).text.toString().trim()} " +
                            "${findViewById<EditText>(R.id.name_shopping_list).text.toString().trim()}"

                    val newDocument = hashMapOf(
                        "items" to mapOf("1" to itemString)
                    )
                    shoppingListRef.add(newDocument)
                        .addOnSuccessListener { documentReference ->
                            homeViewModel.fetchShoppingList()
                            Log.d("shoppingList", "New list created with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("shoppingList", "Error creating new list: $exception")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("shoppingList", "Error fetching documents: $exception")
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