package com.nyenjes.safari.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.nyenjes.safari.R
import com.nyenjes.safari.SafariApplication
import com.nyenjes.safari.adapters.PagerAdapter
import com.nyenjes.safari.managers.FirebaseManager
import com.nyenjes.safari.managers.SafariDataManager
import com.nyenjes.safari.model.Image
import com.nyenjes.safari.model.Place
import com.nyenjes.safari.services.AwsService
import com.nyenjes.safari.services.PlaceService
import com.nyenjes.safari.services.ServiceBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_place_detail.*
import kotlinx.android.synthetic.main.fragment_place_detail.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceDetailFragment : Fragment() {

    private var _firebaseManager: FirebaseManager? = FirebaseManager()
    private var placeService: PlaceService? = null
    private var imageView: ImageView? = null
    private val TAG: String = "PlaceDetailFragment"
    private var awsService: AwsService? = null
    private var placeObject: Place = Place()

    private var root: View? = null

    private var safariManager: SafariDataManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_place_detail, container, false)
        Picasso.with(root!!.context).setIndicatorsEnabled(true)

        safariManager = (activity!!.applicationContext as SafariApplication).safariDataManager

        placeService = ServiceBuilder.buildService(PlaceService::class.java)
        awsService = ServiceBuilder.buildService(AwsService::class.java)

        imageView = root!!.findViewById(R.id.favorite)
        loadFave()

        val place = activity!!.intent.getStringExtra("currentPlaceItem")
        placeObject = Gson().fromJson(place, Place::class.java)

        root!!.textPlaceContent.text = placeObject.content
        getImageFromBucket()

        return root
    }

    private fun getImageFromBucket() {
        val bucketName = "safari-app"
        val baseImageUrl = "https://safari-app.s3-us-west-2.amazonaws.com/"
        val call = awsService!!.getContentsInAwsBucket(bucketName, placeObject.imageUrl!!)

        call.enqueue(object : Callback<List<Image>> {
            override fun onFailure(call: Call<List<Image>>, t: Throwable) {
                Log.d(TAG, "placeService.getImageFromBucket() failed: ${t.message}")
            }

            override fun onResponse(call: Call<List<Image>>, response: Response<List<Image>>) {
                Log.d(TAG, "---------------- response: ------- :${response.body()}")
                var imageUrls = ArrayList<String>()

                for(image in response.body()!!) {
                    val imageName: String? = image.key!!.substringAfter("/")
                    if(imageName != "") {
                        val imageUrl = baseImageUrl + placeObject.imageUrl + "/" + imageName
                        imageUrls.add(imageUrl)
                    }
                }

                val viewPagerAdapter = PagerAdapter(root!!.context, imageUrls)

                detailViewPager.adapter = viewPagerAdapter

            }

        })
    }


    fun loadFave() {
        try {
            if(safariManager!!.getIsFavorite(placeObject.id!!)) {
                imageView!!.setImageResource(R.drawable.ic_fave)
            }
            else {
                imageView!!.setImageResource(R.drawable.ic_fave_border)

            }
        } catch (e: Exception) {
        }
    }

    fun favorite(view: View) {
        Log.d("Here", "Clicked" +placeObject)

        try {
            if(safariManager!!.getIsFavorite(placeObject.id!!)) {
                safariManager!!.removeFavorite(placeObject)
                imageView!!.setImageResource(R.drawable.ic_fave_border)
            }
            else {
                safariManager!!.addFavorite(placeObject)
                imageView!!.setImageResource(R.drawable.ic_fave)

            }
        } catch (e: Exception) {
        }

    }

}
