package com.example.fitnessappkot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeAdapter(private val recipesList: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(recipe: Recipe) {
            itemView.findViewById<TextView>(R.id.recipeNameTextView).text = "Name: ${recipe.name}"
            itemView.findViewById<TextView>(R.id.recipeIngredientsTextView).text = "Ingredients: ${recipe.ingredients.joinToString(separator = "\n")}"
            itemView.findViewById<TextView>(R.id.recipeMacrosTextView).text = "Macros: ${recipe.macros}"
            itemView.findViewById<TextView>(R.id.recipeCaloriesTextView).text = "Calories: ${recipe.calories}"
            itemView.findViewById<TextView>(R.id.recipeStepsTextView).text = "Steps: ${recipe.steps.joinToString(separator = "\n")}"
            itemView.findViewById<TextView>(R.id.recipeDurationTextView).text = "Duration: ${recipe.duration}"
            itemView.findViewById<TextView>(R.id.recipeDifficultyTextView).text = "Difficulty: ${recipe.levelOfDifficulty}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipesList[position]
        holder.bind(recipe)
    }

    override fun getItemCount() = recipesList.size
}
