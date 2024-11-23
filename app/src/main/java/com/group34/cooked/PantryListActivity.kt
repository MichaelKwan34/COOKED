package com.group34.cooked

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class PantryListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry_list)

        // Initialize the ListView
        val listView: ListView = findViewById(R.id.pantryListView)

        val pantryList = intent.getStringArrayListExtra("PANTRY_LIST")

        // Set the adapter
        val adapter = pantryList?.let { PantryListActivityAdapter(this, it) }
        listView.adapter = adapter
    }
}