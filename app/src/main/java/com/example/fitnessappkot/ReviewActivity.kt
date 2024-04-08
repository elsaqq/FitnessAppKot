package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReviewActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var commentInput: EditText
    private lateinit var submitReviewButton: Button
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Setup UI components
        nameInput = findViewById(R.id.nameInput)
        ratingBar = findViewById(R.id.ratingBar)
        commentInput = findViewById(R.id.commentInput)
        submitReviewButton = findViewById(R.id.submitReviewButton)
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView)

        // Setup RecyclerView with LinearLayoutManager and ReviewAdapter
        reviewsRecyclerView.layoutManager = LinearLayoutManager(this)
        val reviewsList = mutableListOf<Review>()
        val adapter = ReviewAdapter(reviewsList)
        reviewsRecyclerView.adapter = adapter

        // Load existing reviews from Firebase
        FirebaseDatabase.getInstance().getReference("reviews")
            .orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    reviewsList.clear()
                    snapshot.children.forEach { child ->
                        val review = child.getValue(Review::class.java)
                        review?.let { reviewsList.add(it) }
                    }
                    reviewsList.reverse() // Display the latest review at the top
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Failed to load reviews.", Toast.LENGTH_SHORT).show()
                }
            })

        // Handle review submission
        submitReviewButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter your name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rating = ratingBar.rating
            val comment = commentInput.text.toString().trim()

            if (comment.isNotEmpty()) {
                // Construct a Review object
                val review = Review(
                    userId = auth.currentUser?.uid ?: "",
                    name = name, // Include the name in the review
                    rating = rating,
                    comment = comment
                )

                // Push the review to Firebase
                FirebaseDatabase.getInstance().getReference("reviews")
                    .push()
                    .setValue(review)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Review submitted successfully.", Toast.LENGTH_SHORT).show()
                        // Clear input fields after successful submission
                        nameInput.text.clear()
                        commentInput.text.clear()
                        ratingBar.rating = 0f
                    }
                    .addOnFailureListener {
                        Toast.makeText(applicationContext, "Failed to submit review.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(applicationContext, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
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
