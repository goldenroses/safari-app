package com.nyenjes.safari.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.nyenjes.safari.R
import com.nyenjes.safari.activities.PlaceDetailActivity
import com.nyenjes.safari.adapters.ReviewsCardAdapter
import com.nyenjes.safari.model.Review
import com.nyenjes.safari.services.AwsService
import com.nyenjes.safari.services.ReviewService
import com.nyenjes.safari.services.ServiceBuilder
import kotlinx.android.synthetic.main.fragment_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewFragment : Fragment() {
    private val TAG: String = "ReviewFragment"

    var awsService: AwsService? = null
    var cardAdapter: ReviewsCardAdapter? = ReviewsCardAdapter()
    var reviewsArray : ArrayList<Review> = ArrayList()
    var recyclerView : RecyclerView? = null
    var progressBar : ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_review, container, false)
        progressBar = view.findViewById(R.id.progressBar)

        awsService = ServiceBuilder.buildService(AwsService::class.java)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = cardAdapter
        showDialog()

        val placeId = (activity as PlaceDetailActivity).placeObject.id!!

        val reviewService = ServiceBuilder.buildService(ReviewService::class.java)

        val call = reviewService.getReviewsByPlaceId(placeId)

        call.enqueue(object : Callback<List<Review>> {
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Log.d(TAG, "reviewService.getReviewsByPlaceId() failed: PlaceId : ${placeId}")
                hideDialog()
                errorMessageCard.isVisible = true
                btnRetry.isVisible = true
            }

            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                Log.d(TAG,"----------------reviewService.getReviewsByPlaceId(): ------- :${response.body()}")

                val jsonReviewsString = Gson().toJson(response.body())
                if(response.body() == null) {
                    hideDialog()
                    textErrorDescription.setTypeface(ResourcesCompat.getFont(context!!, R.font.traveller))
                    btnRetry.setTypeface(ResourcesCompat.getFont(context!!, R.font.traveller))
                    btnRetry.isVisible = true
                    return
                }
                val jsonReviews = Gson().fromJson(jsonReviewsString, Array<Review>::class.java).toList()

                cardAdapter!!.reviews.clear()
                reviewsArray.addAll(jsonReviews)

                cardAdapter!!.reviews = reviewsArray
                if(cardAdapter!!.reviews.size == 0) {
                    val defaultReview = Review(1, "No guides yet", "0", "No guides yet")
                    reviewsArray.add(defaultReview)
                }
                cardAdapter!!.notifyDataSetChanged()
                hideDialog()            }

        })

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_review -> {
                addReviewDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun addReviewDialog(): Boolean {
        val alert = AlertDialog.Builder(context!!)
        alert.setTitle("Do you want to logout?")
        // alert.setMessage("Message");

        alert.setPositiveButton("Ok") { dialog, whichButton ->
            //Your action here
        }

        alert.setNegativeButton(
            "Cancel"
        ) { dialog, whichButton -> }

        alert.show()
        return true
    }

    private fun showDialog() {
        progressBar!!.setVisibility(View.VISIBLE);
    }

    private fun hideDialog() {
        if (progressBar!!.getVisibility() == View.VISIBLE) {
            progressBar!!.setVisibility(View.INVISIBLE);
        }
    }

    fun refreshPage(view: View) {
        fragmentManager!!.beginTransaction().detach(this).attach(this).commit();
        Log.d(TAG, "refreshExplorePage")
    }
}
