package com.nyenjes.safari.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.nyenjes.safari.R
import com.nyenjes.safari.SafariApplication
import com.nyenjes.safari.adapters.ServiceTypeCardAdapter
import com.nyenjes.safari.managers.SafariDataManager
import com.nyenjes.safari.model.Person
import com.nyenjes.safari.model.ServiceType
import com.nyenjes.safari.services.ServiceBuilder
import com.nyenjes.safari.services.ServicesService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_services.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.GridLayoutManager

class ServicesFragment : Fragment() {
    private val TAG: String = "ServicesFragment"
    private var imageView: ImageView? = null

    var cardAdapter: ServiceTypeCardAdapter? = ServiceTypeCardAdapter()
    var servicesArray : ArrayList<ServiceType> = ArrayList()
    var recyclerView : RecyclerView? = null
    var progressBar : ProgressBar? = null

    private var root: View? = null

    private var safariManager: SafariDataManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_services, container, false)
        Picasso.with(root!!.context).setIndicatorsEnabled(true)
        progressBar = root!!.findViewById(R.id.progressBar)

        safariManager = (activity!!.applicationContext as SafariApplication).safariDataManager

        recyclerView = root!!.findViewById(R.id.servicesRecycler)
        recyclerView!!.layoutManager = GridLayoutManager(context, 2)
//        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.adapter = cardAdapter
        showDialog()

        imageView = root!!.findViewById(R.id.favorite)

        val serviceTypeService = ServiceBuilder.buildService(ServicesService::class.java)

        val call = serviceTypeService.getServices()

        call.enqueue(object : Callback<List<ServiceType>> {
            override fun onFailure(call: Call<List<ServiceType>>, t: Throwable) {
                Log.d(TAG, "servicesService.getServices() failed")
                hideDialog()
                errorMessageCard.isVisible = true
                btnRetry.isVisible = true
            }

            override fun onResponse(call: Call<List<ServiceType>>, response: Response<List<ServiceType>>) {
                Log.d(TAG,"----------------servicesService.getServices(): ------- :${response.body()}")

                val jsonServicesString = Gson().toJson(response.body())
                if(response.body() == null) {
                    hideDialog()
                    textErrorDescription.setTypeface(ResourcesCompat.getFont(context!!, R.font.traveller))
                    btnRetry.setTypeface(ResourcesCompat.getFont(context!!, R.font.traveller))
                    btnRetry.isVisible = true
                    return
                }
                val jsonServiceType = Gson().fromJson(jsonServicesString, Array<ServiceType>::class.java).toList()

                cardAdapter!!.services.clear()
                servicesArray.addAll(jsonServiceType)

                cardAdapter!!.services = servicesArray
                if(cardAdapter!!.services.size == 0) {
                    val defaultService = ServiceType(1, "No Services yet", "0", Person(1))
                    servicesArray.add(defaultService)
                }
                cardAdapter!!.notifyDataSetChanged()
                hideDialog()            }

        })

        return root
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
        fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
        Log.d(TAG, "refreshExplorePage")
    }

}
