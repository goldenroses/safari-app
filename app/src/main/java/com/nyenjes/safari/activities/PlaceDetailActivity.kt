package com.nyenjes.safari.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nyenjes.safari.R
import com.nyenjes.safari.fragments.GuideFragment
import com.nyenjes.safari.fragments.PlaceDetailFragment
import com.nyenjes.safari.model.Place
import kotlinx.android.synthetic.main.activity_place_detail.*

class PlaceDetailActivity : AppCompatActivity() {


    private val placeDetailFragment: PlaceDetailFragment
    private val guideFragment: GuideFragment
    private var placeObject: Place = Place()


    private val TAG: String = "PlaceDetailActivity"

    init {
        placeDetailFragment = PlaceDetailFragment()
        guideFragment = GuideFragment()
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_fave, menu)
        return true
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)

        when (item.itemId) {
            R.id.navigation_place -> transaction.replace(R.id.fragment_container, placeDetailFragment)
            R.id.navigation_guide -> transaction.replace(R.id.fragment_container, guideFragment)

        }

        transaction.commit()
        true
    }


}
