package com.nyenjes.safari.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nyenjes.safari.R
import com.nyenjes.safari.model.Review

class ReviewsCardAdapter : RecyclerView.Adapter<ReviewsCardHolder>() {
    var reviews: ArrayList<Review> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsCardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_card_holder, parent, false)
        return ReviewsCardHolder(view)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ReviewsCardHolder, position: Int) {
        var currentItem = reviews[position]
        holder.cardTitle.text = currentItem.title
        holder.cardDecription.text = currentItem.comment
        for (i in 0 until currentItem.rating!!.toInt()) {
            val imageStar = ImageView(holder.itemView.context)

            imageStar.setImageResource(R.drawable.ic_star)
            imageStar.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.yellowAccent))

            holder.imageLinearLayout.addView(imageStar)
        }

        holder.updateCurrentItem(currentItem)
    }

}

class ReviewsCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cardTitle = itemView.findViewById<TextView>(R.id.cardTitle)
    val cardDecription = itemView.findViewById<TextView>(R.id.cardDescription)
    val imageStar = itemView.findViewById<ImageView>(R.id.cardImageView)
    val imageLinearLayout = itemView.findViewById<LinearLayout>(R.id.startsLinearLayout)
    val foundUseful = itemView.findViewById<ImageView>(R.id.reviewImageUpVote)
    val foundNotUseful = itemView.findViewById<ImageView>(R.id.reviewImageDownVote)

    var currentReviewtem: Review? = null

    init {
        foundUseful.setOnClickListener {
            Toast.makeText(itemView.context, "Upvoted", Toast.LENGTH_SHORT).show()
        }

        foundNotUseful.setOnClickListener {
            Toast.makeText(itemView.context, "Downvoted", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateCurrentItem(currentItem: Review) {
        currentReviewtem = currentItem
    }

}