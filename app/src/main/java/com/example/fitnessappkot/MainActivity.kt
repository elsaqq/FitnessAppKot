package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize UI components
        val weightInput = findViewById<EditText>(R.id.weightInput)
        val heightInput = findViewById<EditText>(R.id.heightInput)
        val ageInput = findViewById<EditText>(R.id.ageInput)
        val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)
        val activityLevelSpinner = findViewById<Spinner>(R.id.activityLevelSpinner)
        val goalGroup = findViewById<RadioGroup>(R.id.goalGroup)
        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val resultText = findViewById<TextView>(R.id.resultText)

        ArrayAdapter.createFromResource(
            this,
            R.array.activity_levels,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            activityLevelSpinner.adapter = adapter
        }

        calculateButton.setOnClickListener {
            val weight = weightInput.text.toString().toDoubleOrNull() ?: 0.0
            val height = heightInput.text.toString().toDoubleOrNull() ?: 0.0
            val age = ageInput.text.toString().toIntOrNull() ?: 0
            val selectedGoalId = goalGroup.checkedRadioButtonId
            val goal = findViewById<RadioButton>(selectedGoalId).text.toString()
            val activityLevelPosition = activityLevelSpinner.selectedItemPosition
            val isMale = genderGroup.checkedRadioButtonId == R.id.maleGender

            val macros = calculateMacros(weight, height, age, goal, activityLevelPosition, isMale)
            storeMacros(macros)
            resultText.text = "Calories: ${macros["calories"]}\nProteins: ${macros["proteins"]}g\nCarbs: ${macros["carbs"]}g\nFats: ${macros["fats"]}g"
        }

        findViewById<Button>(R.id.btnViewRecipes).setOnClickListener {
            startActivity(Intent(this, ViewRecipesActivity::class.java))
        }
    }

    private fun calculateMacros(weight: Double, height: Double, age: Int, goal: String, activityLevelPosition: Int, isMale: Boolean): Map<String, Double> {
        // BMR calculation
        val bmr = if (isMale) {
            (10 * weight) + (6.25 * height) - (5 * age) + 5
        } else {
            (10 * weight) + (6.25 * height) - (5 * age) - 161
        }

        // TDEE calculation based on activity level
        val tdeeMultiplier = when (activityLevelPosition) {
            0 -> 1.2    // Sedentary
            1 -> 1.375  // Lightly active
            2 -> 1.55   // Moderately active
            3 -> 1.725  // Very active
            else -> 1.9 // Extra active
        }
        val tdee = bmr * tdeeMultiplier

        // Adjust TDEE based on the goal (lose weight, maintain, gain weight)
        val adjustedCalories = when (goal) {
            "Lose Weight" -> tdee - 500
            "Gain Weight" -> tdee + 500
            else -> tdee // Assume "Maintain Weight" or unspecified
        }

        // Macro split: 30% protein, 30% fats, 40% carbs
        val proteins = (adjustedCalories * 0.30) / 4 // 1g protein = 4 calories
        val fats = (adjustedCalories * 0.30) / 9    // 1g fat = 9 calories
        val carbs = (adjustedCalories * 0.40) / 4   // 1g carb = 4 calories

        return mapOf(
            "calories" to adjustedCalories,
            "proteins" to proteins,
            "carbs" to carbs,
            "fats" to fats
        )
    }


    private fun storeMacros(macros: Map<String, Double>) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            FirebaseDatabase.getInstance().getReference("userMacros").child(currentUser.uid)
                .setValue(macros)
                .addOnSuccessListener {
                    Toast.makeText(this, "Macros stored successfully.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to store macros.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not signed in.", Toast.LENGTH_SHORT).show()
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



        }
        return super.onOptionsItemSelected(item)

    }


}
