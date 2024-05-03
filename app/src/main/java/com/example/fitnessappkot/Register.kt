package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
/**
 * Register activity manages user registration with email and password.
 * It includes input validation and interacts with Firebase Authentication to create new user accounts.
 */
class Register : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var buttonRegister: Button

    /**
     * Sets up the activity layout and initializes UI components and event listeners.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize UI components
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        buttonRegister = findViewById(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val confirmPassword = editTextConfirmPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                if (isValidPassword(password)) {
                    registerUser(email, password)
                } else {
                    Toast.makeText(this, "Password does not meet the requirements.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please check your inputs", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<TextView>(R.id.textViewLogin).setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
    /**
     * Validates the password against a pattern to ensure it meets security requirements.
     * @param password The user's input password.
     * @return True if the password meets the pattern requirements, otherwise false.
     */
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        return password.matches(passwordPattern.toRegex())
    }

    /**
     * Attempts to register a new user with Firebase Authentication.
     * @param email User's email address.
     * @param password User's password.
     */
    private fun registerUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Login::class.java))
                finish()
            } else {
                if (task.exception is FirebaseAuthUserCollisionException) {
                    // Specific handling for email collision
                    Toast.makeText(this, "Email already in use. Please try another email.", Toast.LENGTH_LONG).show()
                } else {
                    // General error handling
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
