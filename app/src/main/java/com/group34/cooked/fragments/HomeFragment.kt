package com.group34.cooked.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.group34.cooked.HomeViewModel
import com.group34.cooked.PantryListActivity
import com.group34.cooked.PantryListAdapter
import com.group34.cooked.R
import com.group34.cooked.ShoppingListActivity
import com.group34.cooked.ShoppingListAdapter
import androidx.fragment.app.activityViewModels

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var shoppingListView: ListView
    private lateinit var pantryListView: ListView
    private lateinit var shoppingAdapter: ShoppingListAdapter
    private lateinit var pantryAdapter: PantryListAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shoppingListView = view.findViewById(R.id.shopping_list_view)
        pantryListView = view.findViewById(R.id.pantry_list_view)

        shoppingAdapter = ShoppingListAdapter(requireContext(), ArrayList())
        shoppingListView.adapter = shoppingAdapter

        pantryAdapter = PantryListAdapter(requireContext(), ArrayList())
        pantryListView.adapter = pantryAdapter

        // Observing shopping list data
        homeViewModel.shoppingList.observe(viewLifecycleOwner, Observer { shoppingList ->
            updateShoppingListView(shoppingList)
        })

        // Observing pantry list data
        homeViewModel.pantryList.observe(viewLifecycleOwner, Observer { pantryList ->
            updatePantryListView(pantryList)
        })

        // Pantry list tab
        val pantryListTab: LinearLayout = view.findViewById(R.id.open_pantry)
        pantryListTab.setOnClickListener {
            val intent = Intent(requireContext(), PantryListActivity::class.java)
            intent.putStringArrayListExtra("PANTRY_LIST", ArrayList(homeViewModel.pantryList.value ?: emptyList()))
            startActivity(intent)
        }

        // Shopping list tab
        val shoppingListTab: LinearLayout = view.findViewById(R.id.open_shopping_list)
        shoppingListTab.setOnClickListener {
            val intent = Intent(requireContext(), ShoppingListActivity::class.java)
            intent.putStringArrayListExtra("SHOPPING_LIST", ArrayList(homeViewModel.shoppingList.value ?: emptyList()))
            startActivity(intent)
        }
    }

    private fun updateShoppingListView(shoppingList: ArrayList<String>) {
        shoppingAdapter.clear()
        shoppingAdapter.addAll(shoppingList)
        shoppingAdapter.notifyDataSetChanged()

        if (shoppingList.size < 4) {
            shoppingListView.layoutParams.height = (shoppingList.size * 128.75).toInt()
        } else {
            shoppingListView.layoutParams.height = 515
        }
    }

    private fun updatePantryListView(pantryList: ArrayList<String>) {
        this.pantryAdapter.clear()
        pantryAdapter.addAll(pantryList)
        pantryAdapter.notifyDataSetChanged()

        if (pantryList.size < 4) {
            pantryListView.layoutParams.height = (pantryList.size * 128.75).toInt()
        } else {
            pantryListView.layoutParams.height = 515
        }
    }

}