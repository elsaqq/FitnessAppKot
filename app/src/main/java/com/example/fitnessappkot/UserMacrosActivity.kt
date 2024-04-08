package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class UserMacrosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_macros)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fetchUserMacros()
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
                    // Ideally, inform the user about the error.
                }
        } else {
            // No user logged in; handle this scenario, perhaps redirect to login screen.
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

            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut() // Sign out from Firebase
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish() // Prevent returning to MainActivity after logging out
                true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
