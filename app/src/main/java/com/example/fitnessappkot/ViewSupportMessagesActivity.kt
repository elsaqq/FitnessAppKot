package com.example.fitnessappkot

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewSupportMessagesActivity : AppCompatActivity() {

    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messagesList: ArrayList<SupportMessage>
    private lateinit var adapter: SupportMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_support_messages)

        messagesRecyclerView = findViewById(R.id.supportMessagesRecyclerView)
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.setHasFixedSize(true)

        messagesList = arrayListOf()
        adapter = SupportMessageAdapter(messagesList)
        messagesRecyclerView.adapter = adapter

        loadMessages()
    }

    private fun loadMessages() {
        val dbRef = FirebaseDatabase.getInstance().getReference("SupportMessages")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesList.clear()
                snapshot.children.forEach {
                    val message = it.getValue(SupportMessage::class.java)
                    message?.let { messagesList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to load messages", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
