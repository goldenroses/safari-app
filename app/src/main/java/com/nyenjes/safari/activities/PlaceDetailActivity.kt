package com.nyenjes.safari.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.view.marginStart
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.nyenjes.safari.R
import com.nyenjes.safari.fragments.GuideFragment
import com.nyenjes.safari.fragments.PlaceDetailFragment
import com.nyenjes.safari.fragments.ReviewFragment
import com.nyenjes.safari.model.Person
import com.nyenjes.safari.model.Place
import com.nyenjes.safari.model.Review
import com.nyenjes.safari.services.ReviewService
import com.nyenjes.safari.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.fragment_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlaceDetailActivity : AppCompatActivity() {

    private val placeDetailFragment: PlaceDetailFragment
    private val guideFragment: GuideFragment
    private val reviewsFragment: ReviewFragment
    var placeObject: Place = Place()

    var reviewService: ReviewService? = null

    private val TAG: String = "PlaceDetailActivity"

    init {
        placeDetailFragment = PlaceDetailFragment()
        guideFragment = GuideFragment()
        reviewsFragment = ReviewFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        setSupportActionBar(toolbar)

        reviewService = ServiceBuilder.buildService(ReviewService::class.java)


        supportActionBar!!.title = placeObject.title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, placeDetailFragment)
        transaction.commit()

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_fave, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareApp()
                true
            }
            R.id.action_add_review -> {
                Toast.makeText(this, "Add review", Toast.LENGTH_SHORT).show()

                addReviewDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun addReviewDialog(): Boolean {
        val textLayout = LinearLayout(this)
        textLayout.orientation = LinearLayout.VERTICAL
        textLayout.layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        textLayout.layoutParams.width = MATCH_PARENT
        textLayout.setPadding(10, 10, 10, 10)

        val titleText = EditText(this)
        val commentText = EditText(this)

        textLayout.addView(titleText)
        textLayout.addView(commentText)

        val ratingLayout = LinearLayout(this)
        ratingLayout.orientation = LinearLayout.HORIZONTAL
        ratingLayout.layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

        val ratingBar = RatingBar(this)
        ratingBar.setPadding(5, 5, 5, 5)
        ratingBar.numStars = 5
        ratingBar.stepSize = 1f
        ratingBar.scaleX = .5f
        ratingBar.scaleY = .5f
        val stars = ratingBar.progressDrawable as LayerDrawable
        stars.getDrawable(2).setColorFilter(Color.argb(1, 255,173,18), PorterDuff.Mode.SRC_ATOP)
        ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            Toast.makeText(this, "Rated: ${ratingBar.rating}", Toast.LENGTH_SHORT).show()
        }

        ratingLayout.addView(ratingBar)
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.textAlignment = LinearLayout.TEXT_ALIGNMENT_CENTER
        layout.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        layout.setPadding(10, 10, 10, 10)

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Add a review")
        layout.addView(ratingLayout)

        layout.addView(textLayout)

        titleText.setHint("Title goes here")
        commentText.setHint("Comment...")

        alert.setView(layout)

        alert.setPositiveButton("Ok") { dialog, whichButton ->
            //POST review
            saveReview(titleText.text.toString(), commentText.text.toString(), ratingBar.rating.toInt())

            Toast.makeText(
                this,
                "Title: ${titleText.text} : Desc: ${commentText.text} : ${ratingBar.rating}",
                Toast.LENGTH_LONG
            ).show()

        }

        alert.setNegativeButton(
            "Cancel"
        ) { dialog, whichButton -> }

        alert.show()
        return false
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)

        when (item.itemId) {
            R.id.navigation_place -> transaction.replace(R.id.fragment_container, placeDetailFragment)
            R.id.navigation_guide -> transaction.replace(R.id.fragment_container, guideFragment)
            R.id.navigation_reviews -> transaction.replace(R.id.fragment_container, reviewsFragment)
        }

        transaction.commit()
        true
    }

    fun shareApp() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus"
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    fun favoritePlace(view: View) {
        placeDetailFragment.favoritePlace(view)
    }

    fun refreshPlaceDetailPage(view: View) {
//        placeDetailFragment.refreshPage(view)
    }

    fun refreshGuidePage(view: View) {
        guideFragment.refreshPage(view)
    }

    fun refreshReviewsPage(view: View) {
        reviewsFragment.refreshPage(view)
    }

    fun refreshServicesPage(view: View) {
        reviewsFragment.refreshPage(view)
    }

    fun saveReview(title: String, comment: String, rating: Int) {
        val review = Review()
        review.title = title
        review.comment = comment
        review.rating = rating.toString()
        review.place = placeObject
        review.person = Person(2)
        val call = reviewService!!.createReview(review)

        call.enqueue(object : Callback<Review> {
            override fun onFailure(request: Call<Review>, t: Throwable) {
                Log.d(TAG, "reviewService.createReview() failed : ${title}")
            }

            override fun onResponse(request: Call<Review>, response: Response<Review>) {
                if (response.body() == null) {
                    textErrorDescription.setTypeface(
                        ResourcesCompat.getFont(
                            this@PlaceDetailActivity,
                            R.font.traveller
                        )
                    )
                    btnRetry.setTypeface(ResourcesCompat.getFont(this@PlaceDetailActivity, R.font.traveller))
                    btnRetry.isVisible = true
                    return
                }

            }
        })
    }

}
