package com.example.fitnessappkot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var editTextEmailLogin: EditText
    private lateinit var editTextPasswordLogin: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewRegister: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        editTextEmailLogin = findViewById(R.id.editTextEmailLogin)
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewRegister = findViewById(R.id.textViewRegister)

        buttonLogin.setOnClickListener {
            val email = editTextEmailLogin.text.toString().trim()
            val password = editTextPasswordLogin.text.toString().trim()
            loginUser(email, password)
        }

        textViewRegister.setOnClickListener {
            // Navigate to Register Activity
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Navigate to Main Activity
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show()
        }
    }
}
