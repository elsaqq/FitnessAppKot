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

/**
 * MainActivity allows users to calculate and store their macro-nutritional goals based on personal parameters.
 * It supports user authentication via Firebase and stores calculated macros in Firebase Database.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    /**
     * Sets up the activity's layout and initializes UI components and Firebase authentication.
     */
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
            resultText.text = "Proteins: ${macros["proteins"]}g\nCarbs: ${macros["carbs"]}g\nFats: ${macros["fats"]}g"
        }

        findViewById<Button>(R.id.btnViewRecipes).setOnClickListener {
            startActivity(Intent(this, ViewRecipesActivity::class.java))
        }
    }
    /**
     * Calculates user-specific macro goals based on provided personal parameters.
     * @param weight User's weight in kg.
     * @param height User's height in cm.
     * @param age User's age in years.
     * @param goal User's dietary goal (e.g., Lose Weight, Gain Weight).
     * @param activityLevelPosition Index of the selected activity level in the spinner.
     * @param isMale Boolean representing if the user is male.
     * @return A map of macro-nutritional values.
     */
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

        val adjustedCalories = when (goal) {
            "Lose Weight" -> tdee - 500
            "Gain Weight" -> tdee + 500
            else -> tdee
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


    /**
     * Stores the calculated macros in Firebase Database under the user's profile.
     * @param macros Map containing calculated macro-nutritional values.
     */
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
    }


}
