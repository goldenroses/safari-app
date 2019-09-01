package com.nyenjes.safari.repositories

import com.nyenjes.safari.model.Place
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.rowParser

class FavoritesRepository(val dbOpenHelper: SafariDbOpenHelper) {

    private val TABLE_NAME = "favorites"

    fun addFavorites(place: Place) {

        dbOpenHelper.use {
            insert(TABLE_NAME,
                "id" to place.id,
                "title" to place.title,
                "description" to place.description,
                "content" to place.content,
                "imageUrl" to place.imageUrl,
                "cardImage" to place.cardImage

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
        return places.any{ place ->
            place.id == placeId
        }

    }

    fun getAllFavorites(): ArrayList<Place> {

        var places = ArrayList<Place>()

        val newsRowParser = rowParser { title: String,
                                        description: String,
                                        content: String,
                                        imageUrl: String,
                                        cardImage: String ->
            val place = Place()

            place.title = title
            place.description = description
            place.content = content
            place.imageUrl = imageUrl
            place.cardImage = cardImage

            places.add(place)

        }
        return places
    }
}