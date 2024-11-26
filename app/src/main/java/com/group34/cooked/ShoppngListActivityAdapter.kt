package com.group34.cooked

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlin.collections.ArrayList

class ShoppngListActivityAdapter(
    private val context: Context,
    private val items: ArrayList<String>
) : ArrayAdapter<String>(context, R.layout.list_item_shopping_list_activity, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_shopping_list_activity, parent, false)

        val checkBox: CheckBox = view.findViewById(R.id.itemCheckBox)
        val textView: TextView = view.findViewById(R.id.itemText)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)

        // Set the item text
        textView.text = items[position]

        // Handle delete button click
        deleteButton.setOnClickListener {
            items.removeAt(position)
            notifyDataSetChanged()
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->

        }

        return view
    }
}
