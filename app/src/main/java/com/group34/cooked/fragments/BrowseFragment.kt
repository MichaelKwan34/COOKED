package com.group34.cooked.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.group34.cooked.NewRecipeActivity
import com.group34.cooked.R

class BrowseFragment : Fragment(R.layout.fragment_browse) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAddRecipe = view.findViewById<Button>(R.id.btn_add_recipe)
        btnAddRecipe.setOnClickListener {
            startActivity(Intent(context, NewRecipeActivity::class.java))
        }
    }
}