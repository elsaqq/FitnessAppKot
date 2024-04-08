package com.example.fitnessappkot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiaryEntryAdapter(private var entries: List<DiaryEntry>) : RecyclerView.Adapter<DiaryEntryAdapter.DiaryEntryViewHolder>() {

    class DiaryEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewFoodName: TextView = itemView.findViewById(R.id.foodNameTextView)
        private val textViewMacros: TextView = itemView.findViewById(R.id.macrosTextView)

        fun bind(diaryEntry: DiaryEntry) {
            textViewFoodName.text = diaryEntry.foodName
            // Assuming you want to display the macros in a single TextView. Adjust as needed.
            textViewMacros.text = "Proteins: ${diaryEntry.proteins}g, Carbs: ${diaryEntry.carbs}g, Fats: ${diaryEntry.fats}g"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryEntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary_entry, parent, false)
        return DiaryEntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryEntryViewHolder, position: Int) {
        holder.bind(entries[position])
    }

    override fun getItemCount(): Int = entries.size

    fun updateEntries(newEntries: List<DiaryEntry>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}
