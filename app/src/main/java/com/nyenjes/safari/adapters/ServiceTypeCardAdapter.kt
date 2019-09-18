package com.nyenjes.safari.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.nyenjes.safari.R
import com.nyenjes.safari.model.ServiceType

class ServiceTypeCardAdapter : RecyclerView.Adapter<ServiceTypeCardHolder>() {
    var services: ArrayList<ServiceType> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceTypeCardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_services_holder, parent, false)
        return ServiceTypeCardHolder(view)
    }

    override fun getItemCount(): Int {
        return services.size
    }

    override fun onBindViewHolder(holder: ServiceTypeCardHolder, position: Int) {
        holder.cardTitle.setTypeface(ResourcesCompat.getFont(holder.itemView.context, R.font.traveller))
        holder.cardDecription.setTypeface(ResourcesCompat.getFont(holder.itemView.context, R.font.cute))

        var currentItem = services[position]
        holder.cardTitle.text = currentItem.title
        holder.cardDecription.text = currentItem.description

        holder.updateCurrentItem(currentItem)
    }

}

class ServiceTypeCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cardTitle = itemView.findViewById<TextView>(R.id.cardTitle)
    val cardDecription = itemView.findViewById<TextView>(R.id.cardDescription)
    val imageStar = itemView.findViewById<ImageView>(R.id.cardImageView)
    val imageLinearLayout = itemView.findViewById<LinearLayout>(R.id.contentLinearLayout)

    var currentReviewtem: ServiceType? = null

    init {
        cardTitle.setOnClickListener {
            Toast.makeText(itemView.context, "${cardTitle.text} selected", Toast.LENGTH_SHORT).show()
            //Send whatsapp message
            //Send email
//            val intent = Intent(itemView.context, PlaceDetailActivity::class.java)
//            intent.putExtra("currentReviewtem", Gson().toJson(currentReviewtem))
//            itemView.context.startActivity(intent)
        }
    }

    fun updateCurrentItem(currentItem: ServiceType) {
        currentReviewtem = currentItem
    }

}