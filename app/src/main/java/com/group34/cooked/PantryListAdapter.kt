package com.group34.cooked

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView

class PantryListAdapter(
    private val context: Context,
    private val items: ArrayList<String>
) : BaseAdapter() {
    fun clear() {
        items.clear()
    }

    fun addAll(newItems: List<String>) {
        items.addAll(newItems)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView: View
        val holder: ViewHolder

        // Reuse the convertView if possible
        if (convertView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item_pantry_fragment, parent, false)
            holder = ViewHolder()
            holder.checkBox = itemView.findViewById(R.id.checkBox)
            holder.textView = itemView.findViewById(R.id.itemText)
            itemView.tag = holder
        } else {
            itemView = convertView
            holder = itemView.tag as ViewHolder
        }

        // Set the data for each item in the list
        holder.textView?.text = items[position]

        return itemView
    }

    // ViewHolder pattern to improve performance
    private class ViewHolder {
        var checkBox: CheckBox? = null
        var textView: TextView? = null
    }
}