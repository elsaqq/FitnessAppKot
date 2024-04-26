package com.example.fitnessappkot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MacrosAndRecipeMatchActivity : AppCompatActivity() {

    private lateinit var userMacros: UserMacros
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var matchedRecipesList: ArrayList<Recipe>
    private lateinit var adapter: RecipeAdapter

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

    private fun fetchUserMacrosAndMatchRecipes() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseDatabase.getInstance().getReference("userMacros").child(userId)
            .get().addOnSuccessListener { dataSnapshot ->
                val userMacrosValue = dataSnapshot.getValue(UserMacros::class.java)
                userMacrosValue?.let {
                    userMacros = it
                    fetchAndMatchRecipes()
                }
            }.addOnFailureListener {
                // Handle failure
            }
    }

    private fun fetchAndMatchRecipes() {
        FirebaseDatabase.getInstance().getReference("recipes")
            .get().addOnSuccessListener { dataSnapshot ->
                val allRecipes = dataSnapshot.children.mapNotNull { it.getValue(Recipe::class.java) }
                val matchedRecipes = matchRecipesToMacros(allRecipes)
                matchedRecipesList.clear()
                matchedRecipesList.addAll(matchedRecipes)
                adapter.notifyDataSetChanged()
            }.addOnFailureListener {
                // Handle failure
            }
    }

    private fun matchRecipesToMacros(recipes: List<Recipe>): List<Recipe> {

        return recipes.filter { recipe ->
            recipe.calories <= userMacros.calories &&
                    recipe.proteins <= userMacros.proteins &&
                    recipe.carbs <= userMacros.carbs &&
                    recipe.fats <= userMacros.fats
        }
    }


}
