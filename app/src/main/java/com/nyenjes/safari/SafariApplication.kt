package com.nyenjes.safari

import android.app.Application
import com.nyenjes.safari.managers.SafariDataManager
import com.nyenjes.safari.repositories.FavoritesRepository
import com.nyenjes.safari.repositories.SafariDbOpenHelper

class SafariApplication: Application() {

    var dbHelper: SafariDbOpenHelper? = null
    var favoritesRepository: FavoritesRepository? = null
    var safariDataManager: SafariDataManager? = null
    private set


    override fun onCreate() {
        super.onCreate()

        dbHelper = SafariDbOpenHelper(applicationContext)
        favoritesRepository= FavoritesRepository(dbHelper!!)
        safariDataManager= SafariDataManager(favoritesRepository!!)
    }
}