package com.nyenjes.safari.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.jakewharton.picasso.OkHttp3Downloader
import com.nyenjes.safari.R
import com.nyenjes.safari.activities.PlaceDetailActivity
import com.nyenjes.safari.managers.PicassoManager
import com.nyenjes.safari.model.Place
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso


class CardAdapter : RecyclerView.Adapter<CardHolder>() {
    var places: ArrayList<Place> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_holder, parent, false)
        return CardHolder(view)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        var currentItem = places[position]
        holder.cardTitle.text = currentItem.title
        holder.cardDecription.text = currentItem.description

        holder.updateCurrentItem(currentItem)

        val picasso = Picasso.Builder(holder.imageCard.context)
            .downloader(OkHttp3Downloader(PicassoManager.buildClient(holder.imageCard.context)))
            .build()

        if (currentItem.cardImage == "yes") {
            val imageLocation =
                """https://safari-app.s3-us-west-2.amazonaws.com/${currentItem.imageUrl}/card.jpeg"""

            picasso.load(imageLocation).centerCrop().resize(90, 90).networkPolicy(NetworkPolicy.OFFLINE).into(
                holder.imageCard,
                object : Callback {

                    override fun onSuccess() {
                        Log.d("Picasso", "Successfully fetched image CACHE")

                    }

                    override fun onError() {
                        //Try again online if cache failed
                        picasso
                            .load(imageLocation).centerCrop().resize(90, 90)
                            .into(holder.imageCard, object : Callback {
                                override fun onSuccess() {
                                    Log.d("Picasso", "Successfully fetched image ONLINE")
                                }

                                override fun onError() {
                                    Log.d("Picasso", "Could not fetch image ONLINE")
                                }
                            })
                    }
                }
            )

        }
    }

}

class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cardTitle = itemView.findViewById<TextView>(R.id.cardTitle)
    val cardDecription = itemView.findViewById<TextView>(R.id.cardDescription)
    val imageCard = itemView.findViewById<ImageView>(R.id.cardImageView)
    val price = itemView.findViewById<TextView>(R.id.textPrice)
    val upvote = itemView.findViewById<TextView>(R.id.textUpvote)
    val downvote = itemView.findViewById<TextView>(R.id.textDownvote)

    var currentPlaceItem: Place? = null

    init {
        cardTitle.setOnClickListener {
            val intent = Intent(itemView.context, PlaceDetailActivity::class.java)
            intent.putExtra("currentPlaceItem", Gson().toJson(currentPlaceItem))
            itemView.context.startActivity(intent)
        }
    }

    fun updateCurrentItem(currentItem: Place) {
        currentPlaceItem = currentItem
    }

}