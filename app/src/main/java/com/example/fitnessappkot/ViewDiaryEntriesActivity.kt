package com.example.fitnessappkot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewDiaryEntriesActivity : AppCompatActivity() {
    private lateinit var diaryEntriesRecyclerView: RecyclerView
    private lateinit var diaryEntriesList: ArrayList<DiaryEntry>
    private lateinit var diaryEntryAdapter: DiaryEntryAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_diary_entries)

        diaryEntriesRecyclerView = findViewById(R.id.diaryEntriesRecyclerView)
        diaryEntriesRecyclerView.layoutManager = LinearLayoutManager(this)
        diaryEntriesRecyclerView.setHasFixedSize(true)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        diaryEntriesList = arrayListOf<DiaryEntry>()
        diaryEntryAdapter = DiaryEntryAdapter(diaryEntriesList)
        diaryEntriesRecyclerView.adapter = diaryEntryAdapter


        getDiaryEntries()
    }

    private fun getDiaryEntries() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val dbRef = FirebaseDatabase.getInstance().getReference("DiaryEntries").orderByChild("userId").equalTo(user.uid)
            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    diaryEntriesList.clear()
                    var totalProteins = 0.0
                    var totalCarbs = 0.0
                    var totalFats = 0.0

                    for (entrySnapshot in snapshot.children) {
                        val diaryEntry = entrySnapshot.getValue(DiaryEntry::class.java)
                        diaryEntry?.let {
                            diaryEntriesList.add(it)
                            totalProteins += it.proteins
                            totalCarbs += it.carbs
                            totalFats += it.fats
                        }
                    }
                    diaryEntryAdapter.notifyDataSetChanged()

                    findViewById<TextView>(R.id.totalProteinsTextView).text = "Total Proteins: ${totalProteins}g"
                    findViewById<TextView>(R.id.totalCarbsTextView).text = "Total Carbs: ${totalCarbs}g"
                    findViewById<TextView>(R.id.totalFatsTextView).text = "Total Fats: ${totalFats}g"
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Failed to load diary entries", Toast.LENGTH_LONG).show()
                }
            })
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
