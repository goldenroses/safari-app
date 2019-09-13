package com.nyenjes.safari.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nyenjes.safari.R

class ServicesFragment : Fragment() {
    private val TAG: String = "ServicesFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_services, container, false)

        return view
    }

    fun refreshPage(view: View) {
        fragmentManager!!.beginTransaction().detach(this).attach(this).commit();
        Log.d(TAG, "refreshExplorePage")
    }

}
