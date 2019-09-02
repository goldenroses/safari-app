package com.nyenjes.safari.managers

import com.nyenjes.safari.model.Place
import com.nyenjes.safari.providers.SafariDataProvider
import com.nyenjes.safari.repositories.FavoritesRepository

class SafariDataManager (private val safariDataProvider: SafariDataProvider,
                         private val favoritesRepository: FavoritesRepository) {

    private var favoritesCache: ArrayList<Place>? = null


    fun getAllFavorites(): ArrayList<Place>? {

        if(favoritesCache == null) {
            favoritesCache = favoritesRepository.getAllFavorites()
        }

        return favoritesCache
    }

    fun addFavorite(place: Place) {

       favoritesCache?.add(place)
        favoritesRepository.addFavorites(place)
    }


    fun removeFavorite(placeId: Long) {

        favoritesRepository.removeFavoriteById(placeId)
        favoritesCache!!.filter {
            it.id != placeId
        } as ArrayList<Place>
    }

    fun getIsFavorite(placeId: Long): Boolean {
        return favoritesRepository.isArticleFavorite(placeId)
    }
}