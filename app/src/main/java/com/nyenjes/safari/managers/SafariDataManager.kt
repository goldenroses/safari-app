package com.nyenjes.safari.managers

import com.nyenjes.safari.model.Place
import com.nyenjes.safari.repositories.FavoritesRepository

class SafariDataManager (private val favoritesRepository: FavoritesRepository) {

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


    fun removeFavorite(place: Place) {
        favoritesRepository.removeFavoriteById(place.id!!)
        favoritesCache!!.remove(place)
        favoritesCache!!.filter {
            it.id != place.id!!
        } as ArrayList<Place>
    }

    fun getIsFavorite(placeId: Long): Boolean {
        return favoritesRepository.isArticleFavorite(placeId)
    }
}