package com.group34.cooked

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.group34.cooked.models.Ingredient
import com.group34.cooked.models.Instruction

// Generic list adapter for displaying a list of items
// Specifically used for displaying ingredients and instructions
// Not configured to handle other types of items
class ListAdapter<T>(private var context: Context, private var list: List<T>): BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): T {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)
        val inflater = LayoutInflater.from(context)

        // Check the item type and inflate the appropriate layout
        return when (item) {
            is Ingredient -> {
                val view = inflater.inflate(R.layout.ingredient_list_item, parent, false)
                val itemTv = view.findViewById<TextView>(R.id.ingredient_item)
                itemTv.text = "${item.quantity} ${item.measurement} ${item.name}"
                view
            }
            is Instruction -> {
                val view = inflater.inflate(R.layout.instruction_list_item, parent, false)
                val stepTv = view.findViewById<TextView>(R.id.instruction_item_step)
                val descriptionTv = view.findViewById<TextView>(R.id.instruction_item_description)

                stepTv.text = "${item.stepNumber}. "
                descriptionTv.text = item.description
                view
            }
            else -> {
                throw IllegalArgumentException("Unsupported item type")
            }
        }
    }

    fun setList(newList: List<T>) {
        if (newList.isEmpty()) return

        list = newList
        notifyDataSetChanged()
    }
}