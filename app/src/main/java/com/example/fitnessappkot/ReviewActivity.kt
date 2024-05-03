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


/**
 * ReviewActivity allows users to submit reviews and view existing reviews.
 * It interacts with Firebase to store and retrieve reviews, displaying them using a RecyclerView.
 */
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
