package com.example.hungerhalter.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.hungerhalter.R
import com.example.hungerhalter.adapter.DashboardRecyclerAdapter
import com.example.hungerhalter.database.RestaurantEntity
import com.example.hungerhalter.model.Restaurant
import com.example.hungerhalter.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    val restaurantList = ArrayList<Restaurant>()
    var restaurantId: String? = "100"

    lateinit var restaurantName: String
    lateinit var restaurantRating: String
    lateinit var restaurantImage: String

    var ratingComparator = Comparator<Restaurant>{ restaurant1, restaurant2 ->
        if(restaurant1.restaurantRating.compareTo(restaurant2.restaurantRating,true) == 0){
            restaurant1.restaurantName.compareTo(restaurant2.restaurantName,true)
        } else {
            restaurant1.restaurantRating.compareTo(restaurant2.restaurantRating,true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        layoutManager = LinearLayoutManager(activity)
        recyclerAdapter = DashboardRecyclerAdapter(activity as Context, restaurantList)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result"

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    try {
                        progressLayout.visibility = View.GONE
                        val json = it.getJSONObject("data")
                        val success = json.getBoolean("success")

                        if (success) {
                            val data = json.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val restaurantJsonObject = data.getJSONObject(i)
                                val restaurantObject = Restaurant(
                                    restaurantJsonObject.getString("id"),
                                    restaurantJsonObject.getString("name"),
                                    restaurantJsonObject.getString("rating"),
                                    restaurantJsonObject.getString("image_url")
                                )

                                /*val checkFav = DashboardRecyclerAdapter.DBAsyncTask(activity as Context,restaurantEntity,1).execute()

                                val isFav = checkFav.get()

                                if(isFav){
                                    val fav = ContextCompat.getDrawable(activity as Context, R.drawable.ic_favorite)
                                    DashboardRecyclerAdapter.DashboardViewHolder(view).imgFavorite.setImageDrawable(fav)
                                } else {
                                    val noFav = ContextCompat.getDrawable(activity as Context,R.drawable.ic_no_fav)
                                    DashboardRecyclerAdapter.DashboardViewHolder(view).imgFavorite.setImageDrawable(noFav)
                                }

                                DashboardRecyclerAdapter.DashboardViewHolder(view).imgFavorite.setOnClickListener {
                                    if(!DashboardRecyclerAdapter.DBAsyncTask(activity as Context,restaurantEntity,1).execute().get()){
                                        val async = DashboardRecyclerAdapter.DBAsyncTask(activity as Context,restaurantEntity,2).execute()

                                        val result = async.get()
                                        if(result){
                                            val fav = ContextCompat.getDrawable(activity as Context, R.drawable.ic_favorite)
                                            DashboardRecyclerAdapter.DashboardViewHolder(view).imgFavorite.setImageDrawable(fav)

                                            Toast.makeText(activity as Context, "Restaurant added to favorites",Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(activity as Context,"Some error occurred",Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        val async = DashboardRecyclerAdapter.DBAsyncTask(activity as Context,restaurantEntity,3).execute()
                                        val result = async.get()

                                        if(result){
                                            val nofav = ContextCompat.getDrawable(activity as Context, R.drawable.ic_no_fav)
                                            DashboardRecyclerAdapter.DashboardViewHolder(view).imgFavorite.setImageDrawable(nofav)

                                            Toast.makeText(activity as Context, "Restaurant removed from favorites",Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(activity as Context,"Some error occurred",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } */

                                restaurantList.add(restaurantObject)
                                recyclerAdapter = DashboardRecyclerAdapter(activity as Context, restaurantList)
                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager
                            }

                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some Error Occurred!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some error might Occurred!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    if (activity != null) {
                        Toast.makeText(activity as Context, "Volley Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "2b48362961bdf5"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        val id = item?.itemId
        if(id == R.id.action_sort){
            Collections.sort(restaurantList,ratingComparator)
            restaurantList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }

}
