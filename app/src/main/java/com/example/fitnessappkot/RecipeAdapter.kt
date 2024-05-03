package com.example.fitnessappkot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitnessappkot.R

/**
 * Adapter for displaying a list of recipes in a RecyclerView.
 * Each item in the list represents a recipe, displaying its name, ingredients, macronutrients, and other details.
 */
class RecipeAdapter(private val recipesList: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {


    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.recipeNameTextView)
        private val ingredientsTextView: TextView = itemView.findViewById(R.id.recipeIngredientsTextView)
        private val carbsTextView: TextView = itemView.findViewById(R.id.recipeCarbosTextView)
        private val proteinTextView: TextView = itemView.findViewById(R.id.recipeProteinTextView)
        private val fatTextView: TextView = itemView.findViewById(R.id.recipeFatTextView)
        private val stepsTextView: TextView = itemView.findViewById(R.id.recipeStepsTextView)
        private val durationTextView: TextView = itemView.findViewById(R.id.recipeDurationTextView)
        private val difficultyTextView: TextView = itemView.findViewById(R.id.recipeDifficultyTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.recipeImageView)

        /**
         * Bind the current view in the ViewHolder to the data in the corresponding Recipe object.
         * @param recipe The Recipe object containing the data to be displayed.
         */
        fun bind(recipe: Recipe) {
            nameTextView.text = "Name: ${recipe.name}"
            ingredientsTextView.text = "Ingredients: ${recipe.ingredients.joinToString(separator = "\n")}"
            carbsTextView.text = "Carbos: ${recipe.carbs}"
            proteinTextView.text = "Protein: ${recipe.proteins}"
            fatTextView.text = "Fat: ${recipe.fats}"
            stepsTextView.text = "Steps: ${recipe.steps.joinToString(separator = "\n")}"
            durationTextView.text = "Duration: ${recipe.duration}"
            difficultyTextView.text = "Difficulty: ${recipe.levelOfDifficulty}"

            Glide.with(itemView.context)
                .load(recipe.pictureUrl)
                .placeholder(R.drawable.backfinal)
                .error(R.drawable.error_placeholder)
                .into(imageView)
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
    /**
     * Returns the size of your dataset.
     */
    override fun getItemCount() = recipesList.size
}
