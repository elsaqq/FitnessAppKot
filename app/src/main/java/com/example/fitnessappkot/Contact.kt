package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * Contact activity allows users to send support messages which are stored in Firebase.
 */
class Contact : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val messageEditText = findViewById<EditText>(R.id.editTextSupportMessage)

        findViewById<Button>(R.id.buttonSendToSupport).setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val message = messageEditText.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && message.isNotEmpty()) {
                sendMessageToSupport(name, email, message)
                nameEditText.text.clear()
                emailEditText.text.clear()
                messageEditText.text.clear()
            } else {
                Toast.makeText(this, "Please enter your name, email, and message.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Sends a user support message to Firebase and handles success or failure.
     * @param name User's name
     * @param email User's email
     * @param message Support message content
     */
    private fun sendMessageToSupport(name: String, email: String, message: String) {
        val supportRef = FirebaseDatabase.getInstance().getReference("SupportMessages")
        val messageId = supportRef.push().key ?: return

        val supportMessage = hashMapOf(
            "id" to messageId,
            "name" to name,
            "email" to email,
            "message" to message,
            "timestamp" to System.currentTimeMillis()
        )

        supportRef.child(messageId).setValue(supportMessage)
            .addOnSuccessListener {
                Toast.makeText(this, "Message sent to support.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to send message to support.", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menucostumer, menu)
        return true
    }

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
                FirebaseAuth.getInstance().signOut()
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
