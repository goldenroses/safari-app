package com.nyenjes.safari.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.nyenjes.safari.R
import com.nyenjes.safari.SafariApplication
import com.nyenjes.safari.adapters.CardAdapter
import com.nyenjes.safari.managers.SafariDataManager
import com.nyenjes.safari.model.Place
import org.jetbrains.anko.doAsync

class FavoritesFragment : Fragment() {

    private val TAG: String = "FavoritesFragment"

    private var faveRecycler: RecyclerView? = null
    private var faveManager: SafariDataManager? = null
    private var cardAdapter: CardAdapter? = CardAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_favorites, container, false)
        faveRecycler = view.findViewById(R.id.recycler)

        faveRecycler!!.layoutManager = LinearLayoutManager(view.context)

        faveRecycler!!.adapter = cardAdapter

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        faveManager= (activity!!.applicationContext as SafariApplication).safariDataManager
    }

    override fun onResume() {
        super.onResume()

        try {
            var favePlaces = faveManager?.getAllFavorites()
            cardAdapter!!.places.clear()
            cardAdapter!!.places.addAll(favePlaces as ArrayList<Place>)
            if(cardAdapter!!.places.size == 0) {
                var noPlace = ArrayList<Place>()
                val defaultPlace = Place(1, "No Faves Yet")
                noPlace.add(defaultPlace)
                cardAdapter!!.places = noPlace
            }
            activity!!.runOnUiThread { cardAdapter!!.notifyDataSetChanged() }
        }
        catch (e: Exception) {
            val builder = AlertDialog.Builder(view!!.context)
            builder.setMessage(e.message).setTitle("oops! error occured")

            var dialog = builder.create()
            dialog.show()

        }
    }

    fun refreshPage(view: View) {
        fragmentManager!!.beginTransaction().detach(this).attach(this).commit();
        Log.d(TAG, "refreshExplorePage")
    }

}
