package com.group34.cooked

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.group34.cooked.models.Ingredient
import com.group34.cooked.models.Instruction


class RecipeAdapter<T>(
    private var list: List<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // View types to differentiate between Ingredient and Instruction
    private companion object {
        const val VIEW_TYPE_INGREDIENT = 0
        const val VIEW_TYPE_INSTRUCTION = 1
    }

    // Define ViewHolder for Ingredient
    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientTextView: TextView = itemView.findViewById(R.id.ingredient_item)
    }

    // Define ViewHolder for Instruction
    class InstructionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stepTextView: TextView = itemView.findViewById(R.id.instruction_item_step)
        val descriptionTextView: TextView = itemView.findViewById(R.id.instruction_item_description)
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is Ingredient -> VIEW_TYPE_INGREDIENT
            is Instruction -> VIEW_TYPE_INSTRUCTION
            else -> throw IllegalArgumentException("Unsupported item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_INGREDIENT -> {
                val view = inflater.inflate(R.layout.ingredient_list_item, parent, false)

                val deleteButton: ImageView = view.findViewById(R.id.ingredient_item_delete)
                deleteButton.visibility = View.GONE

                IngredientViewHolder(view)
            }
            VIEW_TYPE_INSTRUCTION -> {
                val view = inflater.inflate(R.layout.instruction_list_item, parent, false)
                val deleteButton: ImageView = view.findViewById(R.id.instruction_item_delete)
                deleteButton.visibility = View.GONE
                InstructionViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unsupported view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is Ingredient -> {
                val ingredientHolder = holder as IngredientViewHolder
                ingredientHolder.ingredientTextView.text = "${item.quantity} ${item.measurement.unit} ${item.name}"
            }
            is Instruction -> {
                val instructionHolder = holder as InstructionViewHolder
                instructionHolder.stepTextView.text = "${item.stepNumber}. "
                instructionHolder.descriptionTextView.text = item.description
                // Log.d("InstructionAdapter", "Description: ${item.description}")
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(newList: List<T>) {
        list = newList
        notifyDataSetChanged()
    }
}