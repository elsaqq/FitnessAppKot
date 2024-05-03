package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.widget.Toast

/**
 * Activity to match recipes based on user macros. It fetches user-specific macro goals and available recipes,
 * then filters and displays recipes that fit within those macro constraints.
 */
class MacrosAndRecipeMatchActivity : AppCompatActivity() {

    private lateinit var userMacros: UserMacros
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var matchedRecipesList: ArrayList<Recipe>
    private lateinit var adapter: RecipeAdapter

    /**
     * Sets up the activity's layout and initial state,
     * including RecyclerView for displaying recipes.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_macros_and_recipe_match)

        recipesRecyclerView = findViewById(R.id.matchedRecipesRecyclerView)
        recipesRecyclerView.layoutManager = LinearLayoutManager(this)
        recipesRecyclerView.setHasFixedSize(true)

        matchedRecipesList = arrayListOf<Recipe>()
        adapter = RecipeAdapter(matchedRecipesList)
        recipesRecyclerView.adapter = adapter

        fetchUserMacrosAndMatchRecipes()
    }

    /**
     * Fetches the user's macros and all available recipes, then filters and updates the UI with matched recipes.
     */
    private fun fetchUserMacrosAndMatchRecipes() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userMacrosRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("userMacros").child(userId)
        val recipesRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("recipes")

        // Fetch user macros
        userMacrosRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userMacrosSnapshot = task.result
                val userMacros = userMacrosSnapshot?.getValue(UserMacros::class.java)
                if (userMacros != null) {
                    this.userMacros = userMacros

                    // Fetch recipes
                    recipesRef.get().addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            val recipesSnapshot = task2.result
                            val allRecipes = recipesSnapshot?.children?.mapNotNull { it.getValue(Recipe::class.java) } ?: emptyList()

                            val matchedRecipes = matchRecipes(allRecipes, userMacros)
                            updateMatchedRecipes(matchedRecipes)
                        } else {
                            Toast.makeText(applicationContext, "Failed to load recipes", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Failed to load user macros", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Updates the RecyclerView with the new list of matched recipes.
     * @param newMatchedRecipes List of recipes that match the user's macros.
     */
    private fun updateMatchedRecipes(newMatchedRecipes: List<Recipe>) {
        val diffResult = DiffUtil.calculateDiff(RecipeDiffCallback(matchedRecipesList, newMatchedRecipes))

        matchedRecipesList.clear()
        matchedRecipesList.addAll(newMatchedRecipes)

        diffResult.dispatchUpdatesTo(adapter)
    }
    /**
     * Filters recipes to match them against the user's macros.
     * @param allRecipes List of all available recipes.
     * @param userMacros User's current macro nutritional goals.
     * @return List of recipes that match the user's macros.
     */
    private fun matchRecipes(allRecipes: List<Recipe>, userMacros: UserMacros): List<Recipe> {
        // Simple greedy-like approach: Select recipes that minimize deviation from user macros
        val sortedRecipes = allRecipes.sortedByDescending { it.calories + it.proteins + it.carbs + it.fats }
        val matchedRecipes = mutableListOf<Recipe>()
        var remainingMacros = userMacros

        for (recipe in sortedRecipes) {
            if (fitsMacros(remainingMacros, recipe)) {
                matchedRecipes.add(recipe)
                remainingMacros = subtractMacros(remainingMacros, recipe)
            }
        }

        return matchedRecipes
    }
    /**
     * Checks if a recipe fits within the given macros.
     * @param macros User's macros.
     * @param recipe Recipe to check.
     * @return True if the recipe fits within the macros, false otherwise.
     */

    private fun fitsMacros(macros: UserMacros, recipe: Recipe): Boolean {
        return macros.calories >= recipe.calories &&
                macros.proteins >= recipe.proteins &&
                macros.carbs >= recipe.carbs &&
                macros.fats >= recipe.fats
    }

    private fun scoreRecipe(macros: UserMacros, recipe: Recipe): Double {
        val calorieScore = 1.0 - (recipe.calories.toDouble() / macros.calories)
        val proteinScore = 1.0 - (recipe.proteins.toDouble() / macros.proteins)
        val carbScore = 1.0 - (recipe.carbs.toDouble() / macros.carbs)
        val fatScore = 1.0 - (recipe.fats.toDouble() / macros.fats)
        return (calorieScore + proteinScore + carbScore + fatScore) / 4.0
    }

    private fun subtractMacros(macros: UserMacros, recipe: Recipe): UserMacros {
        return UserMacros(
            calories = macros.calories - recipe.calories,
            proteins = macros.proteins - recipe.proteins,
            carbs = macros.carbs - recipe.carbs,
            fats = macros.fats - recipe.fats
        )
    }

    class RecipeDiffCallback(
        private val oldList: List<Recipe>,
        private val newList: List<Recipe>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldRecipe = oldList[oldItemPosition]
            val newRecipe = newList[newItemPosition]
            return oldRecipe.name == newRecipe.name && oldRecipe.ingredients == newRecipe.ingredients
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menucostumer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_view_recipes -> {
                val intent = Intent(this, ViewRecipesActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_view_macros -> {
                val intent = Intent(this, UserMacrosActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_calculate_macro -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_contact -> {
                val intent = Intent(this, Contact::class.java)
                startActivity(intent)
                true
            }

            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut() // Sign out from Firebase
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish() // Prevent returning to MainActivity after logging out
                true
            }


            R.id.action_view_macros_and_recipes -> {
                val intent = Intent(this, MacrosAndRecipeMatchActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_add_data_entry -> {
                val intent = Intent(this, AddDiaryEntryActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_daily_tracker -> {
                val intent = Intent(this, ViewDiaryEntriesActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_feedback -> {
                val intent = Intent(this, ReviewActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_dashboard -> {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }}


