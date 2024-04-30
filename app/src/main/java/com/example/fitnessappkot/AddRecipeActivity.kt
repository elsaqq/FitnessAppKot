package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        database = FirebaseDatabase.getInstance().getReference("recipes")

        findViewById<Button>(R.id.submitRecipeButton).setOnClickListener {
            uploadRecipe()
        }
    }

    private fun uploadRecipe() {
        val name = findViewById<EditText>(R.id.nameEditText).text.toString().trim()
        val ingredients = findViewById<EditText>(R.id.ingredientsEditText).text.toString().split("\\n")
        val carbs = findViewById<EditText>(R.id.CarbosEditText).text.toString().toIntOrNull() ?: 0
        val fats = findViewById<EditText>(R.id.FatsEditText).text.toString().toIntOrNull() ?: 0
        val proteins = findViewById<EditText>(R.id.ProteinEditText).text.toString().toIntOrNull() ?: 0
        val calories = findViewById<EditText>(R.id.caloriesEditText).text.toString().toIntOrNull() ?: 0
        val steps = findViewById<EditText>(R.id.stepsEditText).text.toString().split("\\n")
        val duration = findViewById<EditText>(R.id.durationEditText).text.toString().trim()
        val levelOfDifficulty = findViewById<EditText>(R.id.difficultyEditText).text.toString().trim()
        val pictureUrl = findViewById<EditText>(R.id.pictureUrlEditText).text.toString().trim()

        val recipe = Recipe(name, ingredients, carbs, fats, proteins, calories, steps, duration, levelOfDifficulty, pictureUrl)

        val newRecipeRef = database.push()
        newRecipeRef.setValue(recipe)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Recipe added successfully", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed to add recipe", Toast.LENGTH_LONG).show()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_add_recipe -> {
                val intent = Intent(this, AddRecipeActivity::class.java)
                startActivity(intent)
                true
            }


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
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
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


            R.id.action_dashboard -> {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }

}