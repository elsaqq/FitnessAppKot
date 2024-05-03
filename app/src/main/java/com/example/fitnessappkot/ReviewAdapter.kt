package com.example.fitnessappkot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for displaying reviews in a RecyclerView.
 * Each review displays the username, a rating, and a text comment.
 */
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

    /**
     * Binds data from the review list to elements of the ViewHolder.
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.reviewUsername.text = review.name
        holder.reviewRating.rating = review.rating
        holder.reviewComment.text = review.comment
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    override fun getItemCount() = reviewList.size
}
