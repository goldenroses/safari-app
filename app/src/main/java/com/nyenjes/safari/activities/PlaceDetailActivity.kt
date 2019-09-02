package com.nyenjes.safari.activities

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
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
import kotlinx.android.synthetic.main.activity_place_detail.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class PlaceDetailActivity : AppCompatActivity() {

    private var _firebaseManager: FirebaseManager? = FirebaseManager()
    private var placeObject: Place = Place()
    private var placeService: PlaceService? = null
    private var awsService: AwsService? = null
    private var imageView: ImageView? = null
    private val TAG: String = "MainActivity"

    private var safariManager: SafariDataManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        Picasso.with(this).setIndicatorsEnabled(true)

        safariManager = (applicationContext as SafariApplication).safariDataManager

        val place = intent.getStringExtra("currentPlaceItem")
        placeObject = Gson().fromJson(place, Place::class.java)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = placeObject.title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        placeService = ServiceBuilder.buildService(PlaceService::class.java)
        awsService = ServiceBuilder.buildService(AwsService::class.java)

        getImageFromBucket()
        imageView = findViewById(R.id.favorite)
        loadFave()

        textContentDetail.text = placeObject.content
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

                val viewPagerAdapter = PagerAdapter(this@PlaceDetailActivity, imageUrls)

                detailViewPager.adapter = viewPagerAdapter

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_fave, menu)
        return true
    }

    fun loadFave() {
        try {
            if(safariManager!!.getIsFavorite(placeObject.id!!)) {
                imageView!!.setImageResource(R.drawable.ic_fave)
                toast("placeObject.id is in favorites!")
            }
            else {
                imageView!!.setImageResource(R.drawable.ic_fave_border)
                toast("placeObject.id NOT in favorites!")

            }
        } catch (e: Exception) {
            toast("Unable to fetch fave icon : ${e}")
        }
    }

    fun favorite(view: View) {
        Log.d("Here", "Clicked" +placeObject)

        try {
            if(safariManager!!.getIsFavorite(placeObject.id!!)) {
                safariManager!!.removeFavorite(placeObject)
                imageView!!.setImageResource(R.drawable.ic_fave_border)

                toast("Place removed from favorites!")
            }
            else {
                safariManager!!.addFavorite(placeObject)
                imageView!!.setImageResource(R.drawable.ic_fave)
                toast("Place added to favorites!")

            }
        } catch (e: Exception) {
            toast("Unable to update : ${e}")
        }

    }
}
