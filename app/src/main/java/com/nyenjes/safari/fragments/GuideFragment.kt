package com.nyenjes.safari.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.nyenjes.safari.R
import com.nyenjes.safari.activities.PlaceDetailActivity
import com.nyenjes.safari.adapters.GuideCardAdapter
import com.nyenjes.safari.model.Guide
import com.nyenjes.safari.services.AwsService
import com.nyenjes.safari.services.GuideService
import com.nyenjes.safari.services.ServiceBuilder
import kotlinx.android.synthetic.main.fragment_guide.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuideFragment : Fragment() {

    private val TAG: String = "GuideFragment"

    var awsService: AwsService? = null
    var adapter: GuideCardAdapter? = GuideCardAdapter()
    var guideArray : ArrayList<Guide> = ArrayList()
    var recyclerView : RecyclerView? = null
    var progressBar : ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_guide, container, false)
        progressBar = view.findViewById(R.id.progressBar)

        awsService = ServiceBuilder.buildService(AwsService::class.java)

        recyclerView = view.findViewById(R.id.guideRecycler)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = adapter
        showDialog()

        val placeId = (activity as PlaceDetailActivity).placeObject.id!!

        val guideService = ServiceBuilder.buildService(GuideService::class.java)

        val call = guideService.getGuides()

        call.enqueue(object : Callback<List<Guide>> {
            override fun onFailure(call: Call<List<Guide>>, t: Throwable) {
                Log.d(TAG, "GuideFragment.getServices() failed: PlaceId : ${placeId}")
                hideDialog()
                textErrorDescription.setTypeface(ResourcesCompat.getFont(context!!, R.font.traveller))
                btnRetry.setTypeface(ResourcesCompat.getFont(context!!, R.font.traveller))
                errorMessageCard.isVisible = true
            }

            override fun onResponse(call: Call<List<Guide>>, response: Response<List<Guide>>) {
                Log.d(TAG,"----------------GuideFragment.getServices(): ------- :${response.body()}")

                val jsonReviewsString = Gson().toJson(response.body())
                if(response.body() == null) {
                    hideDialog()
                    textErrorDescription.setTypeface(ResourcesCompat.getFont(context!!, R.font.traveller))
                    btnRetry.setTypeface(ResourcesCompat.getFont(context!!, R.font.traveller))
                    errorMessageCard.isVisible = true
                    return
                }
                val jsonReviews = Gson().fromJson(jsonReviewsString, Array<Guide>::class.java).toList()

                adapter!!.guides.clear()
                guideArray.addAll(jsonReviews)

                adapter!!.guides = guideArray
                if(adapter!!.guides.size == 0) {
                    val defaultGuide = Guide(1, "No services yet", "0", 1,1,1)
                    guideArray.add(defaultGuide)
                }
                adapter!!.notifyDataSetChanged()
                hideDialog()            }
        })

        return view
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
