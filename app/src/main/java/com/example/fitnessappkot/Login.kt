package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnessappkot.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


/**
 * LoginActivity manages user authentication, including admin access features.
 */
class Login : AppCompatActivity() {


    private var adminCode: String = "1234"
    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    /**
    * Initializes the activity, setting up the layout and Firebase authentication,
    along with UI interaction listeners.
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize Firebase authentication.

        firebaseAuth = FirebaseAuth.getInstance()


        // UI Elements for admin switch and admin code input field.

        val switchAdmin = binding.switch1
        val adminCodeField = binding.admincode

        adminCodeField.visibility = View.INVISIBLE
        switchAdmin.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                adminCodeField.visibility = View.VISIBLE
                Toast.makeText(applicationContext, "Insert Admin Code", Toast.LENGTH_SHORT).show()
            } else {
                adminCodeField.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, "Admin Code Hidden", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmailLogin.text.toString().trim()
            val password = binding.editTextPasswordLogin.text.toString().trim()
            val enteredAdminCode = adminCodeField.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (enteredAdminCode == adminCode) {
                        val adminIntent = Intent(this, DashboardActivity::class.java)
                        startActivity(adminIntent)
                    } else {
                        val userIntent = Intent(this, CostumerDash::class.java)
                        startActivity(userIntent)
                    }
                } else {
                    Toast.makeText(this, task.exception?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.textViewRegister.setOnClickListener {
            val registerIntent = Intent(this, Register::class.java)
            startActivity(registerIntent)
        }

        binding.textViewForgotPassword.setOnClickListener {
            val resetPasswordIntent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(resetPasswordIntent)
        }
    }
}














































