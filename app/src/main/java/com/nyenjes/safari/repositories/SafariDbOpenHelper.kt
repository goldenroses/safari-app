package com.nyenjes.safari.repositories

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class SafariDbOpenHelper(context: Context): ManagedSQLiteOpenHelper(context, "safari.db", null, 1) {

    private val TABLE_NAME: String = "favorites"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable( TABLE_NAME,  true,
            "id" to INTEGER + PRIMARY_KEY,
            "title" to TEXT,
            "description" to TEXT,
            "imageUrl" to TEXT,
            "cardImage" to TEXT,
            "content" to TEXT
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}