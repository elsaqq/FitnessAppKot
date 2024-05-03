package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * ResetPasswordActivity allows users to reset their passwords by entering their registered email.
 * It uses Firebase Authentication to send a password reset email.
 */
class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var editTextEmailReset: EditText
    private lateinit var buttonResetPassword: Button
    private lateinit var buttonBackToLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        editTextEmailReset = findViewById(R.id.editTextEmailReset)
        buttonResetPassword = findViewById(R.id.buttonResetPassword)
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin)

        buttonResetPassword.setOnClickListener {
            val email = editTextEmailReset.text.toString().trim()
            if (email.isNotEmpty()) {
                resetPassword(email)
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }

        buttonBackToLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }
    /**
    * Initiates a password reset request for the provided email address using Firebase Authentication.
    * @param email The email address for which to send the reset password link.
    */
    private fun resetPassword(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Failed to send reset email: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
