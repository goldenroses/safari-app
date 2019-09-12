package com.nyenjes.safari.adapters

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val imageStar = ImageView(holder.itemView.context)
            imageStar.setLayoutParams(params)

            imageStar.setImageResource(R.drawable.ic_star)
            imageStar.setColorFilter(Color.YELLOW, PorterDuff.Mode.LIGHTEN)
            holder.imageLinearLayout.addView(imageStar)
        }

        holder.updateCurrentItem(currentItem)
    }

}

class ReviewsCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cardTitle = itemView.findViewById<TextView>(R.id.cardTitle)
    val cardDecription = itemView.findViewById<TextView>(R.id.cardDescription)
    val imageStar = itemView.findViewById<ImageView>(R.id.cardImageView)
    val imageLinearLayout = itemView.findViewById<LinearLayout>(R.id.linearLayout)
    val foundUseful = itemView.findViewById<ImageView>(R.id.imageDownVote)
    val foundNotUseful = itemView.findViewById<ImageView>(R.id.textfoundNotUseful)

    var currentReviewtem: Review? = null

//    init {
//        cardTitle.setOnClickListener {
//            val intent = Intent(itemView.context, PlaceDetailActivity::class.java)
//            intent.putExtra("currentReviewtem", Gson().toJson(currentReviewtem))
//            itemView.context.startActivity(intent)
//        }
//    }

    fun updateCurrentItem(currentItem: Review) {
        currentReviewtem = currentItem
    }

}