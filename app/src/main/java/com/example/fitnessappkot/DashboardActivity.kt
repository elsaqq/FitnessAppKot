
package com.example.fitnessappkot
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class DashboardActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        findViewById<Button>(R.id.MacrosCalculator).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.DiaryEntry1).setOnClickListener {
            startActivity(Intent(this, AddDiaryEntryActivity::class.java))
        }

        findViewById<Button>(R.id.RecipeMatch).setOnClickListener {
            startActivity(Intent(this, MacrosAndRecipeMatchActivity::class.java))
        }

        findViewById<Button>(R.id.Recipes1).setOnClickListener {
            startActivity(Intent(this, ViewRecipesActivity::class.java))
        }

        findViewById<Button>(R.id.Reviews1).setOnClickListener {
            startActivity(Intent(this, ReviewActivity::class.java))
        }


        findViewById<Button>(R.id.viewMessage).setOnClickListener {
            startActivity(Intent(this, ViewSupportMessagesActivity::class.java))
        }


        findViewById<Button>(R.id.AddRecipe1).setOnClickListener {
            startActivity(Intent(this, AddRecipeActivity::class.java))
        }


        findViewById<Button>(R.id.ViewDataEntry1).setOnClickListener {
            startActivity(Intent(this, ViewDiaryEntriesActivity::class.java))
        }

        findViewById<Button>(R.id.viewMacro).setOnClickListener {
            startActivity(Intent(this, UserMacrosActivity::class.java))
        }

        findViewById<Button>(R.id.contact1).setOnClickListener {
            startActivity(Intent(this, Contact::class.java))
        }








    }


}
