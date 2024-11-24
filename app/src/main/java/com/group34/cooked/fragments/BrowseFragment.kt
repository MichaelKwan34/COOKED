package com.group34.cooked.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.group34.cooked.ChatActivity
import com.group34.cooked.R

class BrowseFragment : Fragment(R.layout.fragment_browse) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val generateButton: Button = view.findViewById(R.id.button_generate)
        generateButton.setOnClickListener {
            Log.d("BrowseFragment", "Generate button clicked")
            startChat()
        }
    }

    private fun startChat() {
        val intent = Intent(requireContext(), ChatActivity::class.java)
        startActivity(intent)
    }
}
