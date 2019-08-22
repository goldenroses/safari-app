package com.nyenjes.safari.adapters

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.jakewharton.picasso.OkHttp3Downloader
import com.nyenjes.safari.managers.PicassoManager
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PagerAdapter(val context:Context, val imageUrls: ArrayList<String>): PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)

        val picasso = Picasso.Builder(context)
            .downloader(OkHttp3Downloader(PicassoManager.buildClient(context)))
            .build()

        picasso.load(imageUrls[position]).fit().into(imageView,
            object : Callback {

                override fun onSuccess() {
                    Log.d("Picasso", "Successfully fetched image CACHE")

                }

                override fun onError() {
                    //Try again online if cache failed
                    picasso
                        .load(imageUrls[position]).fit()
                        .into(imageView, object : Callback {
                            override fun onSuccess() {
                                Log.d("Picasso", "Successfully fetched image ONLINE")

                            }

                            override fun onError() {
                                Log.d("Picasso", "Could not fetch image ONLINE")
                            }
                        })
                }
            })

        container.addView(imageView)

        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}
