package com.example.fitnessappkot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val gifImageView: ImageView = findViewById(R.id.gifImageView)

        Glide.with(this)
            .load("https://media1.tenor.com/m/gJZGSe6Fb2UAAAAC/curls-gym.gif")
            .into(gifImageView)

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






    }
}
