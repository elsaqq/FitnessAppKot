package com.example.fitnessappkot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter(private val reviewList: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reviewUsername: TextView = view.findViewById(R.id.reviewUsername)
        val reviewRating: RatingBar = view.findViewById(R.id.reviewRating)
        val reviewComment: TextView = view.findViewById(R.id.reviewComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.reviewUsername.text = review.name
        holder.reviewRating.rating = review.rating
        holder.reviewComment.text = review.comment
    }


    override fun getItemCount() = reviewList.size
}
