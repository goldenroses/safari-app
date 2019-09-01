package com.nyenjes.safari.activities

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.google.gson.Gson
import com.nyenjes.safari.R
import com.nyenjes.safari.adapters.PagerAdapter
import com.nyenjes.safari.managers.FirebaseManager
import com.nyenjes.safari.model.Image
import com.nyenjes.safari.model.Place
import com.nyenjes.safari.services.AwsService
import com.nyenjes.safari.services.PlaceService
import com.nyenjes.safari.services.ServiceBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_place_detail.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        Picasso.with(this).setIndicatorsEnabled(true)

        val place = intent.getStringExtra("currentPlaceItem")
        placeObject = Gson().fromJson(place, Place::class.java)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = placeObject.title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        placeObject.id
        placeService = ServiceBuilder.buildService(PlaceService::class.java)
        awsService = ServiceBuilder.buildService(AwsService::class.java)

        getImageFromBucket()

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

    private fun getImage() {
        _firebaseManager!!.connectStorage(placeObject.title!!)
        val placeRef = _firebaseManager!!._firebaseStorageReference!!

        val localFile = File.createTempFile("images", "jpg")
        placeRef.getFile(localFile).addOnSuccessListener(object : OnSuccessListener<FileDownloadTask.TaskSnapshot> {
            override fun onSuccess(downloadTask: FileDownloadTask.TaskSnapshot?) {

                val downloadUri = downloadTask!!.storage.downloadUrl


                if (downloadUri.isSuccessful) {
//                        val downloadPath09 = downloadUri.result!!.path.toString()
//                        val downloadPath01 = downloadUri.result!!.encodedPath.toString()
                    val downloadPath = downloadUri.toString()
                    Log.d("DOWNLOADING IMAGES", ": imageUrl : ${downloadPath}")
                    //Save image

//                        showImage(downloadPath)

                } else {
                    Log.d("DOWNLOADING IMAGE", "failure!")
                    Toast.makeText(detailViewPager.context, "Image upload failed, try again", Toast.LENGTH_LONG).show()

                }
            }
        }).addOnFailureListener {
            Log.d("UPLOAD IMAGE", "failure! : ${it.message}")

        }
    }


    fun showImage(url: String) {

        if (url.isEmpty() == false) {
            val width = Resources.getSystem().displayMetrics.widthPixels
//            Picasso.with(this).load(url).resize(width, width*2/3).centerCrop().into(detailViewPager)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_fave, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        try {
            if (item!!.itemId == android.R.id.home) {
                finish()
            } else if (item.itemId == R.id.action_favorite) {
//                if(!neuzDataManager!!.getIsFavorite(neuzData!!.title)) {
                Toast.makeText(this, "Adding to favorite", Toast.LENGTH_SHORT).show()
//                    val response = neuzDataManager!!.addFavorites(neuzData!!)
//                }else {
                Toast.makeText(this, "Removing from favorite", Toast.LENGTH_SHORT).show()

//
//                    neuzDataManager!!.removeFavorites(neuzData!!.title)
//                }

            }

        } catch (ex: Exception) {

        }

        return true
    }

    companion object {
        const val PICTURE_RESULT_CODE = 34

    }
}
