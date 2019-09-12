package com.nyenjes.safari.activities

import android.app.Service
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nyenjes.safari.fragments.ExploreFragment
import com.nyenjes.safari.fragments.FavoritesFragment
import com.nyenjes.safari.fragments.ReviewFragment
import com.nyenjes.safari.R
import com.nyenjes.safari.fragments.ServicesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val exploreFragment: ExploreFragment
    private val favoritesFragment: FavoritesFragment
    private val servicesFragment: ServicesFragment

    init {
        exploreFragment = ExploreFragment()
        favoritesFragment = FavoritesFragment()
        servicesFragment = ServicesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Safari"

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, exploreFragment)
        transaction.commit()

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)

        when (item.itemId) {
            R.id.navigation_explore -> transaction.replace(R.id.fragment_container, exploreFragment)
            R.id.navigation_favorites -> transaction.replace(R.id.fragment_container, favoritesFragment)
            R.id.navigation_services -> transaction.replace(R.id.fragment_container, servicesFragment)

        }

        transaction.commit()
        true
    }

    fun refreshPage(view: View) {
        exploreFragment.refreshPage(view)
    }

    fun refreshReviewPage(view: View) {
        servicesFragment.refreshPage(view)
    }
}
