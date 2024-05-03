package com.example.fitnessappkot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
/**
 * Adapter for displaying support messages in a RecyclerView.
 * Each item shows the sender's name, email, and message text.
 */
class SupportMessageAdapter(private val messagesList: List<SupportMessage>) : RecyclerView.Adapter<SupportMessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_support_message, parent, false)
        return MessageViewHolder(view)
    }
    /**
     * Binds data to the views in the ViewHolder.
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messagesList[position]
        holder.nameTextView.text = message.name
        holder.emailTextView.text = message.email
        holder.messageTextView.text = message.message
    }
    /**
     * Returns the size of the data set.
     */
    override fun getItemCount() = messagesList.size
}

