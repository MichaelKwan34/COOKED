package com.group34.cooked.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.group34.cooked.NewRecipeActivity
import com.group34.cooked.R

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigates to the new recipe activity
        view.findViewById<Button>(R.id.plusButton)
            .setOnClickListener {
                val intent = Intent(context, NewRecipeActivity::class.java)
                startActivity(intent);
            }
    }
}