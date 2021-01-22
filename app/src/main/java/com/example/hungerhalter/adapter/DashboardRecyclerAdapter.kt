package com.example.hungerhalter.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.hungerhalter.R
import com.example.hungerhalter.activity.FoodDetailsActivity
import com.example.hungerhalter.database.HungerHalter
import com.example.hungerhalter.database.RestaurantEntity
import com.example.hungerhalter.fragment.HomeFragment
import com.example.hungerhalter.model.Restaurant
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Restaurant>): RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {
    class DashboardViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtRestaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val txtRestaurantRating: TextView = view.findViewById(R.id.txtRestaurantRating)
        val imgRestaurantImage: ImageView = view.findViewById(R.id.imgRestaurantImage)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
        val imgFavorite: ImageView = view.findViewById(R.id.imgFavorite)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.txtRestaurantName.text = restaurant.restaurantName
        holder.txtRestaurantRating.text = restaurant.restaurantRating

        Picasso.get().load(restaurant.restaurantImage).error(R.mipmap.ic_launcher).into(holder.imgRestaurantImage)
        holder.llContent.setOnClickListener {
            val intent = Intent(context, FoodDetailsActivity::class.java)
            intent.putExtra("restaurantId",restaurant.restaurantId)
            intent.putExtra("restaurantName",restaurant.restaurantName)
            context.startActivity(intent)
        }

        /*val restaurantEntity = RestaurantEntity(
            HomeFragment().restaurantId?.toInt() as Int ,
            HomeFragment().restaurantName,
            HomeFragment().restaurantRating,
            HomeFragment().restaurantImage
        )

        val checkFav = DBAsyncTask(context,restaurantEntity,1).execute()

        val isFav = checkFav.get()

        if(isFav){
            val fav = ContextCompat.getDrawable(context, R.drawable.ic_favorite)
            holder.imgFavorite.setImageDrawable(fav)
        } else {
            val noFav = ContextCompat.getDrawable(context,R.drawable.ic_no_fav)
            holder.imgFavorite.setImageDrawable(noFav)
        }

        holder.imgFavorite.setOnClickListener {
            if(!DBAsyncTask(context,restaurantEntity,1).execute().get()){
                val async = DBAsyncTask(context,restaurantEntity,2).execute()

                val result = async.get()
                if(result){
                    val fav = ContextCompat.getDrawable(context, R.drawable.ic_favorite)
                    holder.imgFavorite.setImageDrawable(fav)

                    Toast.makeText(context, "Restaurant added to favorites",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,"Some error occurred",Toast.LENGTH_SHORT).show()
                }
            } else {
                val async = DBAsyncTask(context,restaurantEntity,3).execute()
                val result = async.get()

                if(result){
                    val nofav = ContextCompat.getDrawable(context, R.drawable.ic_no_fav)
                    holder.imgFavorite.setImageDrawable(nofav)

                    Toast.makeText(context, "Restaurant removed from favorites",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,"Some error occurred",Toast.LENGTH_SHORT).show()
                }
            }
        } */
    }


    /*class DBAsyncTask(val context: Context,val restaurantEntity: RestaurantEntity,val mode: Int): AsyncTask<Void, Void, Boolean>(){
        val db = Room.databaseBuilder(context,HungerHalter::class.java,"hungerhalter").build()
        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode) {
                1 -> {
                    val restaurant: RestaurantEntity? = db.restaurantDao().getRestaurantById(restaurantEntity.restaurantId.toString())
                    db.close()

                    return restaurant != null
                }

                2 -> {
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()

                    return true
                }

                3 -> {
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()

                    return true
                }
            }
            return false
        }

    } */
}