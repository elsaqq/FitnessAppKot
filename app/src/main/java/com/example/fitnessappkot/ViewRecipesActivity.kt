package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewRecipesActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var recipesList: ArrayList<Recipe> // Ensure Recipe class is defined elsewhere

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_recipes)

        // Set the toolbar as the app bar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize RecyclerView
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView)
        recipesRecyclerView.layoutManager = LinearLayoutManager(this)
        recipesRecyclerView.setHasFixedSize(true)

        recipesList = arrayListOf<Recipe>()
        getRecipesData()
    }

    private fun getRecipesData() {
        dbRef = FirebaseDatabase.getInstance().getReference("recipes")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recipesList.clear()
                for (recipeSnapshot in snapshot.children) {
                    val recipe = recipeSnapshot.getValue(Recipe::class.java)
                    recipe?.let { recipesList.add(it) }
                }
                recipesRecyclerView.adapter = RecipeAdapter(recipesList) // Ensure RecipeAdapter is defined
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to load recipes", Toast.LENGTH_LONG).show()
            }
        })
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

