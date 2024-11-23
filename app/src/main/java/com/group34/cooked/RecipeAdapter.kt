package com.group34.cooked

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.group34.cooked.models.Recipe
import com.group34.cooked.R


class RecipeAdapter(
    private var recipes: List<Recipe> = emptyList(),
    private val onItemClick: (Recipe) -> Unit,
    private val context: Context
    ) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.recipe_name)
        val courseTextView: TextView = view.findViewById(R.id.recipe_course)
        val servingSizeTextView: TextView = view.findViewById(R.id.recipe_serving_size)
        val difficultyChip: Chip = view.findViewById(R.id.recipe_difficulty)
        val durationChip: Chip = view.findViewById(R.id.recipe_duration)
        val imageView: ImageView = view.findViewById(R.id.recipe_image)


        fun bind(recipe: Recipe) {
            nameTextView.text = recipe.name
            courseTextView.text = recipe.course

            servingSizeTextView.text = context.getString(R.string.serving_size_template, recipe.servings)

            // setting the tag colours and text
            difficultyChip.text = recipe.difficulty
            when (recipe.difficulty.lowercase()) {
                "easy" -> {
                    difficultyChip.setChipBackgroundColorResource(R.color.blueAccent)
                    difficultyChip.setChipStrokeColorResource(R.color.blueAccent)
                }
                "medium" -> {
                    difficultyChip.setChipBackgroundColorResource(R.color.yellowAccent)
                    difficultyChip.setChipStrokeColorResource(R.color.yellowAccent)
                }
                "hard" -> {
                    difficultyChip.setChipBackgroundColorResource(R.color.redAccent)
                    difficultyChip.setChipStrokeColorResource(R.color.redAccent)
                }
                // Default to medium
                else -> {
                    difficultyChip.setChipBackgroundColorResource(R.color.yellowAccent)
                    difficultyChip.setChipStrokeColorResource(R.color.yellowAccent)
                }

            }
            durationChip.text = context.getString(R.string.duration_template, recipe.duration)
            when {
                // 30 mins or less
                recipe.duration <= 30 -> {
                    durationChip.setChipBackgroundColorResource(R.color.blueAccent)
                    durationChip.setChipStrokeColorResource(R.color.blueAccent)
                }
                // 1 hour or less
                recipe.duration <= 60 -> {
                    durationChip.setChipBackgroundColorResource(R.color.yellowAccent)
                    durationChip.setChipStrokeColorResource(R.color.yellowAccent)
                }
                // > 1 hour
                else -> {
                    durationChip.setChipBackgroundColorResource(R.color.yellowAccent)
                    durationChip.setChipStrokeColorResource(R.color.yellowAccent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_preview_item, parent, false)
        Log.d("RecipeAdapter", "onCreateViewHolder called")
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    // For updating the list of entries as it changes
    fun submitList(newEntries: List<Recipe>) {
        recipes = newEntries
        notifyDataSetChanged()
    }

    override fun getItemCount() = recipes.size
}