package com.group34.cooked

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ShoppingListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        // Initialize the ListView
        val listView: ListView = findViewById(R.id.shoppingListView)

        val shoppingList = intent.getStringArrayListExtra("SHOPPING_LIST")
        // Set the adapter
        val adapter = shoppingList?.let { ShoppngListActivityAdapter(this, it) }
        listView.adapter = adapter
    }
}
