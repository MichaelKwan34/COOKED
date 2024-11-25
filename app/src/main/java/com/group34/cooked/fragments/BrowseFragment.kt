package com.group34.cooked.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.group34.cooked.ChatActivity
import com.group34.cooked.NewRecipeActivity
import com.group34.cooked.R

class BrowseFragment : Fragment(R.layout.fragment_browse) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle Generate Button
        val generateButton: Button = view.findViewById(R.id.button_generate)
        generateButton.setOnClickListener {
            Log.d("BrowseFragment", "Generate button clicked")
            startChat()
        }

        // Handle Add Recipe Button
        val btnAddRecipe = view.findViewById<Button>(R.id.btn_add_recipe)
        btnAddRecipe.setOnClickListener {
            startActivity(Intent(context, NewRecipeActivity::class.java))
        }

        // Handle Filter Icon click to show sorting options
        val sortIcon: ImageView = view.findViewById(R.id.sort_icon)
        sortIcon.setOnClickListener {
            // Create the popup menu
            val popupMenu = PopupMenu(requireContext(), sortIcon)
            // Inflate the menu resource
            popupMenu.menuInflater.inflate(R.menu.sort_menu, popupMenu.menu)

            // Set up a listener for menu item clicks
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sort_main_dish -> {
                        // Handle "Main Dish" sorting
                        Log.d("BrowseFragment", "Sorted by Main Dish")
                        // You can implement logic to filter/display only Main Dishes here
                        true
                    }
                    R.id.sort_appetizer -> {
                        // Handle "Appetizer" sorting
                        Log.d("BrowseFragment", "Sorted by Appetizer")
                        // Implement logic to filter/display only Appetizers here
                        true
                    }
                    R.id.sort_dessert -> {
                        // Handle "Dessert" sorting
                        Log.d("BrowseFragment", "Sorted by Dessert")
                        // Implement logic to filter/display only Desserts here
                        true
                    }
                    else -> false
                }
            }

            // Show the popup menu
            popupMenu.show()
        }
    }

    // Start ChatActivity when the generate button is clicked
    private fun startChat() {
        val intent = Intent(requireContext(), ChatActivity::class.java)
        startActivity(intent)
    }
}
