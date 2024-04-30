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
