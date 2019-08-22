package com.nyenjes.safari.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.nyenjes.safari.R
import com.nyenjes.safari.adapters.CardAdapter
import com.nyenjes.safari.model.Place
import com.nyenjes.safari.services.PlaceService
import com.nyenjes.safari.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"

    private var recycler: RecyclerView? = null
    private val adapter: CardAdapter? = CardAdapter()
    var places : ArrayList<Place> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Places"

        // Fetch Places
        val placeService = ServiceBuilder.buildService(PlaceService::class.java)

        val call = placeService.getPlaces()

        recycler = findViewById(R.id.recycler)
        recycler!!.layoutManager = LinearLayoutManager(this)
        recycler!!.adapter = adapter
        showDialog()

        call.enqueue(object: Callback<List<Place>> {
            override fun onFailure(call: retrofit2.Call<List<Place>>, t: Throwable) {
                Log.d(TAG, "placeService.getPlaces() failed")
                hideDialog()
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

                places.addAll(jsonPlaces)
                adapter!!.places.clear()

                adapter.places = places
                adapter.notifyDataSetChanged()
                hideDialog()
            }

        })

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_explore -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_reviews -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_admin -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun showDialog() {
        progressBar!!.setVisibility(View.VISIBLE);

    }

    private fun hideDialog() {
        if (progressBar!!.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    fun refreshPage(view: View) {
        Log.d(TAG, "refreshPage")
        finish()
        startActivity(intent)
    }
}
