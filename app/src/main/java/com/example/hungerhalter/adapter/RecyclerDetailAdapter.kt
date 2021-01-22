package com.example.hungerhalter.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.core.content.ContextCompat.*
import androidx.recyclerview.widget.RecyclerView
import com.example.hungerhalter.R
import com.example.hungerhalter.R.layout.activity_food_details
import com.example.hungerhalter.activity.CartActivity
import com.example.hungerhalter.activity.FoodDetailsActivity
import com.example.hungerhalter.model.Food
import com.google.android.material.snackbar.Snackbar

class RecyclerDetailAdapter(val context: Context, val itemList: ArrayList<Food>): RecyclerView.Adapter<RecyclerDetailAdapter.DetailViewHolder>() {
    class DetailViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtFoodName: TextView = view.findViewById(R.id.txtFoodName)
        val txtFoodPrice: TextView = view.findViewById(R.id.txtFoodPrice)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_details_single_row,parent,false)

        return DetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val food = itemList[position]
        holder.txtFoodName.text = food.foodName
        holder.txtFoodPrice.text = food.foodPrice
        holder.btnAdd.setOnClickListener{
            if(holder.btnAdd.text == "Add") {
                holder.btnAdd.text = "Remove"
                val addColor = getColor(context,R.color.colorAccent)
                holder.btnAdd.setBackgroundColor(addColor)
                val txtColor = getColor(context,R.color.colorPrimary)
                holder.btnAdd.setTextColor(txtColor)

                val snackbar = Snackbar.make(it,"Item added to cart",Snackbar.LENGTH_INDEFINITE).setAction("Cart"){
                    val intent = Intent(context, CartActivity::class.java)
                    context.startActivity(intent)
                }
                snackbar.show()


            } else {
                holder.btnAdd.text = "Add"
                val addColor = getColor(context,R.color.colorPrimary)
                holder.btnAdd.setBackgroundColor(addColor)
                val txtColor = getColor(context,R.color.colorAccent)
                holder.btnAdd.setTextColor(txtColor)

                val snackbar = Snackbar.make(it,"Item removed from cart",Snackbar.LENGTH_INDEFINITE).setAction("Cart"){
                    val intent = Intent(context, CartActivity::class.java)
                    context.startActivity(intent)
                }
                snackbar.show()
            }


        }

    }
}