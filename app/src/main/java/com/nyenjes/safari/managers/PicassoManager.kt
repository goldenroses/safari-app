package com.nyenjes.safari.managers

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.io.File
import java.util.*

class PicassoManager {

    companion object {
        fun buildClient(context: Context): OkHttpClient {
            val httpCacheDirectory = File(context.cacheDir, "picasso-cache")
            val createdCache = Cache(httpCacheDirectory, 15 * 1024 * 1024)

            val okClient = OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1)).cache(createdCache)
                .build()

            return okClient
        }
    }
}
