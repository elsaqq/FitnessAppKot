package com.example.fitnessappkot
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class UserMacrosActivity : AppCompatActivity() {

    private lateinit var matchRecipesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_macros)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fetchUserMacros()

        // Initialize the match recipes button
        matchRecipesButton = findViewById(R.id.matchRecipesButton)
        matchRecipesButton.setOnClickListener {
            startActivity(Intent(this, MacrosAndRecipeMatchActivity::class.java))
        }
    }

    private fun fetchUserMacros() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseDatabase.getInstance().getReference("userMacros").child(userId)
                .get().addOnSuccessListener { dataSnapshot ->
                    val userMacros = dataSnapshot.getValue<UserMacros>()
                    userMacros?.let {
                        findViewById<TextView>(R.id.caloriesText).text = "Calories: ${it.calories}"
                        findViewById<TextView>(R.id.proteinsText).text = "Proteins: ${it.proteins}"
                        findViewById<TextView>(R.id.carbsText).text = "Carbs: ${it.carbs}"
                        findViewById<TextView>(R.id.fatsText).text = "Fats: ${it.fats}"
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to retrieve user macros", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show()
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
            R.id.action_view_macros_and_recipes -> {
                startActivity(Intent(this, MacrosAndRecipeMatchActivity::class.java))
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
                Intent(this, Login::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(it)
                }
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