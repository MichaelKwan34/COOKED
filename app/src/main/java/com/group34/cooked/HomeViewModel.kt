package com.group34.cooked

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {

    private val _shoppingList = MutableLiveData<ArrayList<String>>()
    val shoppingList: LiveData<ArrayList<String>> = _shoppingList

    private val _pantryList = MutableLiveData<ArrayList<String>>()
    val pantryList: LiveData<ArrayList<String>> = _pantryList

    init {
        fetchShoppingList()
        fetchPantryList()
    }

    private fun fetchShoppingList() {
        val db = FirebaseFirestore.getInstance()
        val shoppingListRef = db.collection("shoppingList")

        shoppingListRef.get().addOnSuccessListener { querySnapshot ->
            val shoppingList = ArrayList<String>()
            for (document in querySnapshot.documents) {
                val shoppingListMap = document.data
                shoppingListMap?.values?.forEach { item ->
                    if (item is String) {
                        shoppingList.add(item)
                    }
                }
            }
            _shoppingList.value = shoppingList
            Log.d("shoppingList", "Shopping List: $shoppingList")
        }.addOnFailureListener { exception ->
            Log.e("shoppingList", "Error fetching shopping list: $exception")
        }
    }

    private fun fetchPantryList() {
        val db = FirebaseFirestore.getInstance()
        val pantryListRef = db.collection("pantryList")

        pantryListRef.get().addOnSuccessListener { querySnapshot ->
            val pantryList = ArrayList<String>()
            for (document in querySnapshot.documents) {
                val pantryListMap = document.data
                pantryListMap?.values?.forEach { item ->
                    if (item is String) {
                        pantryList.add(item)
                    }
                }
            }
            _pantryList.value = pantryList
            Log.d("pantryList", "Pantry List: $pantryList")
        }.addOnFailureListener { exception ->
            Log.e("pantryList", "Error fetching pantry list: $exception")
        }
    }
}
