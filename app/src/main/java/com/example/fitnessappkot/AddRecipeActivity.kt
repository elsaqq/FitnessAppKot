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

        // Set the toolbar as the app bar
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
        val macros = findViewById<EditText>(R.id.macrosEditText).text.toString().trim()
        val calories = findViewById<EditText>(R.id.caloriesEditText).text.toString().toIntOrNull() ?: 0
        val steps = findViewById<EditText>(R.id.stepsEditText).text.toString().split("\\n")
        val duration = findViewById<EditText>(R.id.durationEditText).text.toString().trim()
        val levelOfDifficulty = findViewById<EditText>(R.id.difficultyEditText).text.toString().trim()

        val pictureUrl = "http://example.com/image.jpg" // Placeholder for an image URL

        val recipe = Recipe(name, ingredients, macros, calories, steps, duration, levelOfDifficulty, pictureUrl)

        val newRecipeRef = database.push()
        newRecipeRef.setValue(recipe)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Recipe added successfully", Toast.LENGTH_LONG).show()
                finish() // Optionally, close this activity on success
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
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the action for the Up button
                finish()
                return true
            }
            R.id.action_add_recipe -> {
                startActivity(Intent(this, AddRecipeActivity::class.java))
                return true
            }
            R.id.action_view_recipes -> {
                startActivity(Intent(this, ViewRecipesActivity::class.java))
                return true
            }
            R.id.action_view_macros -> {
                startActivity(Intent(this, UserMacrosActivity::class.java))

                return true
            }

            R.id.addDataEntry -> {
                startActivity(Intent(this, AddDiaryEntryActivity::class.java))

                return true
            }

            R.id.DailyTracker -> {
                startActivity(Intent(this, ViewDiaryEntriesActivity::class.java))

                return true
            }

            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut() // Sign out from Firebase
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish() // Prevent returning to MainActivity after logging out
                true
            }

            R.id.Feedback -> {
                startActivity(Intent(this, ReviewActivity::class.java))

                return true
            }

            R.id.Dashboard -> {
                startActivity(Intent(this, DashboardActivity::class.java))

                return true
            }

            R.id.action_calculatemacro-> {
                startActivity(Intent(this, MainActivity::class.java))

                return true
            }



        }
        return super.onOptionsItemSelected(item)

    }
}
