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

class AddDiaryEntryActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary_entry)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Firebase Auth instance
        auth = FirebaseAuth.getInstance()

        val editTextFoodName: EditText = findViewById(R.id.editTextFoodName)
        val editTextQuantity: EditText = findViewById(R.id.editTextQuantity)
        val editTextProteins: EditText = findViewById(R.id.editTextProteins)
        val editTextCarbs: EditText = findViewById(R.id.editTextCarbs)
        val editTextFats: EditText = findViewById(R.id.editTextFats)
        val spinnerMealTime: Spinner = findViewById(R.id.spinnerMealTime)
        val buttonSaveEntry: Button = findViewById(R.id.buttonSaveEntry)

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

    private fun saveDiaryEntry(diaryEntry: DiaryEntry) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("DiaryEntries")
        val entryId = databaseReference.push().key ?: return // Generate a unique ID

        // Create a new DiaryEntry with the ID
        val newEntryWithId = diaryEntry.copy(id = entryId)

        databaseReference.child(entryId).setValue(newEntryWithId)
            .addOnSuccessListener {
                Toast.makeText(this, "Entry saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save entry", Toast.LENGTH_SHORT).show()
            }
    }

}

