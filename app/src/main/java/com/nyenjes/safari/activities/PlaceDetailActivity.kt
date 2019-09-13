package com.nyenjes.safari.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nyenjes.safari.R
import com.nyenjes.safari.fragments.ExploreFragment
import com.nyenjes.safari.fragments.GuideFragment
import com.nyenjes.safari.fragments.PlaceDetailFragment
import com.nyenjes.safari.fragments.ReviewFragment
import com.nyenjes.safari.model.Place
import kotlinx.android.synthetic.main.activity_place_detail.*

class PlaceDetailActivity : AppCompatActivity() {

    private val placeDetailFragment: PlaceDetailFragment
    private val guideFragment: GuideFragment
    private val reviewsFragment: ReviewFragment
    var placeObject: Place = Place()

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
            else -> super.onOptionsItemSelected(item)
        }

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

}
