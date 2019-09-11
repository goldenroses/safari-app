package com.nyenjes.safari.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.nyenjes.safari.R
import com.nyenjes.safari.adapters.CardAdapter
import com.nyenjes.safari.model.Place
import com.nyenjes.safari.services.PlaceService
import com.nyenjes.safari.services.ServiceBuilder
import kotlinx.android.synthetic.main.fragment_explore.*
import retrofit2.Callback
import retrofit2.Response

class ExploreFragment : Fragment() {

    private val TAG: String = "ExploreFragment"

    private var recycler: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private val adapter: CardAdapter? = CardAdapter()
    var places : ArrayList<Place> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        progressBar = view.findViewById(R.id.exploreProgressBar)
        showDialog()
        // Fetch Places
        val placeService = ServiceBuilder.buildService(PlaceService::class.java)

        val call = placeService.getPlaces()
        call.enqueue(object: Callback<List<Place>> {
            override fun onFailure(call: retrofit2.Call<List<Place>>, t: Throwable) {
                Log.d(TAG, "placeService.getReviews() failed")
                hideDialog()
                errorMessageCard.isVisible = true
                btnRetry.isVisible = true
            }

            override fun onResponse(call: retrofit2.Call<List<Place>>, response: Response<List<Place>>) {
                Log.d(TAG,"---------------- response: ------- :${response.body()}")

                val jsonPlacesString = Gson().toJson(response.body())
                if(response.body() == null) {
                    btnRetry.isVisible = true
                    hideDialog()
                    return
                }
                val jsonPlaces = Gson().fromJson(jsonPlacesString, Array<Place>::class.java).toList()

                adapter!!.places.clear()
                places.addAll(jsonPlaces)

                adapter.places = places
                adapter.notifyDataSetChanged()
                hideDialog()
            }
        })

        recycler = view.findViewById(R.id.recycler)
        recycler!!.layoutManager = LinearLayoutManager(context)
        recycler!!.adapter = adapter
//        showDialog()

        // Inflate the layout for this fragment
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
        Log.d(TAG, "refreshPage")
    }
}
