package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Setup listeners for your buttons to navigate to different activities
        findViewById<Button>(R.id.MacrosCalculator).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.DiaryEntry1).setOnClickListener {
            startActivity(Intent(this, AddDiaryEntryActivity::class.java))
        }

        findViewById<Button>(R.id.Recipes1).setOnClickListener {
            startActivity(Intent(this, ViewRecipesActivity::class.java))
        }

        findViewById<Button>(R.id.Reviews1).setOnClickListener {
            startActivity(Intent(this, ReviewActivity::class.java))
        }

        findViewById<Button>(R.id.ViewDataEntry1).setOnClickListener {
            startActivity(Intent(this, ViewDiaryEntriesActivity::class.java))
        }

        findViewById<Button>(R.id.viewMacro).setOnClickListener {
            startActivity(Intent(this, UserMacrosActivity::class.java))
        }

        // Send message to support
        val messageEditText = findViewById<EditText>(R.id.editTextSupportMessage)
        findViewById<Button>(R.id.buttonSendToSupport).setOnClickListener {
            val message = messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessageToSupport(message)
                messageEditText.text.clear()
            } else {
                Toast.makeText(this, "Please enter a message.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendMessageToSupport(message: String) {
        val supportRef = FirebaseDatabase.getInstance().getReference("SupportMessages")
        val messageId = supportRef.push().key ?: return  // Generate a unique ID for the message

        val supportMessage = hashMapOf(
            "id" to messageId,
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
}
