package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

/**
 * Activity for adding a new diary entry associated with user's meal.
 * This includes fields for food name, quantity, macronutrients, and the meal time.
 * It interacts with Firebase to store diary entries and update user macros.
 */
class AddDiaryEntryActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    /**
     * Initializes the activity with UI elements and Firebase setup.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary_entry)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()

        // Binding UI elements to variables.

        val editTextFoodName: EditText = findViewById(R.id.editTextFoodName)
        val editTextQuantity: EditText = findViewById(R.id.editTextQuantity)
        val editTextProteins: EditText = findViewById(R.id.editTextProteins)
        val editTextCarbs: EditText = findViewById(R.id.editTextCarbs)
        val editTextFats: EditText = findViewById(R.id.editTextFats)
        val spinnerMealTime: Spinner = findViewById(R.id.spinnerMealTime)
        val buttonSaveEntry: Button = findViewById(R.id.buttonSaveEntry)

        // Setting up the spinner for selecting meal time.

        ArrayAdapter.createFromResource(
            this,
            R.array.meal_times,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerMealTime.adapter = adapter
        }

        buttonSaveEntry.setOnClickListener {
            val foodName = editTextFoodName.text.toString().trim()
            val quantity = editTextQuantity.text.toString().trim()
            val proteins = editTextProteins.text.toString().toDoubleOrNull() ?: 0.0
            val carbs = editTextCarbs.text.toString().toDoubleOrNull() ?: 0.0
            val fats = editTextFats.text.toString().toDoubleOrNull() ?: 0.0
            val mealTime = spinnerMealTime.selectedItem.toString()

            if (foodName.isEmpty() || quantity.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val diaryEntry = DiaryEntry(
                userId = auth.currentUser?.uid ?: "",
                foodName = foodName,
                quantity = quantity,
                mealTime = mealTime,
                proteins = proteins,
                carbs = carbs,
                fats = fats
            )
            saveDiaryEntry(diaryEntry)
        }
    }

    /**
     * Saves the diary entry to Firebase and updates user macros.
     */
    private fun saveDiaryEntry(diaryEntry: DiaryEntry) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("DiaryEntries")
        val entryId = databaseReference.push().key ?: return // Generate a unique ID

        val newEntryWithId = diaryEntry.copy(id = entryId)

        databaseReference.child(entryId).setValue(newEntryWithId)
            .addOnSuccessListener {
                Toast.makeText(this, "Entry saved successfully", Toast.LENGTH_SHORT).show()

                // Update the user's macros in Firebase
                val userMacrosRef = FirebaseDatabase.getInstance().getReference("userMacros").child(diaryEntry.userId)
                userMacrosRef.get().addOnSuccessListener { dataSnapshot ->
                    val userMacros = dataSnapshot.getValue<UserMacros>()
                    userMacros?.let {
                        val updatedUserMacros = UserMacros(
                            calories = it.calories,
                            proteins = it.proteins - diaryEntry.proteins,
                            carbs = it.carbs - diaryEntry.carbs,
                            fats = it.fats - diaryEntry.fats
                        )
                        userMacrosRef.setValue(updatedUserMacros)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save entry", Toast.LENGTH_SHORT).show()
            }
    }
    /**
     * Inflates the menu for this activity with specific actions.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menucostumer, menu)
        return true
    }
    /**
     * Handles item selections from the inflated menu.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
            else -> super.onOptionsItemSelected(item)
        }
    }

}




