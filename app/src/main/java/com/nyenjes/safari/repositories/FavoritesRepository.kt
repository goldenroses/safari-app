package com.nyenjes.safari.repositories

import com.nyenjes.safari.model.Place
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

private val logger: AnkoLogger = AnkoLogger<String>()

class FavoritesRepository(val dbOpenHelper: SafariDbOpenHelper) {

    private val TABLE_NAME = "favorites"

    fun addFavorites(place: Place) {

        dbOpenHelper.use {
            insert(
                TABLE_NAME,
                "id" to place.id,
                "title" to place.title,
                "description" to place.description,
                "imageUrl" to place.imageUrl,
                "cardImage" to place.cardImage,
                "content" to place.content
                )
        }
    }

    fun removeFavoriteById(placeId: Long) {
        dbOpenHelper.use {
            delete(TABLE_NAME, "id = {placeId}", "placeId" to placeId)
        }
    }

    fun isArticleFavorite(placeId: Long): Boolean {
        var places = getAllFavorites()
        return places!!.any { place ->
            place.id == placeId
        }

    }

    data class PlaceItem(
        var id: Int,
        var title: String,
        var description: String,
        var imageUrl: String,
        var cardImage: String,
        var content: String
    )

    fun getAllFavorites(): ArrayList<Place>? {

        var places = ArrayList<Place>()

        dbOpenHelper.use {
            var results = select(TABLE_NAME)
            var data = results.parseList(classParser<Place>())
            places.addAll(data)
        }

        return places
    }

}
