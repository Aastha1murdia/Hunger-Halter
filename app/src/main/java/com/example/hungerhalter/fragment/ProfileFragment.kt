package com.example.hungerhalter.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.hungerhalter.R

class ProfileFragment : Fragment() {

    lateinit var txtName: TextView
    lateinit var txtNumber: TextView
    lateinit var txtAddress: TextView
    lateinit var btnMyOrders: Button
    lateinit var btnFavorite: Button
    lateinit var sharedPreferences: SharedPreferences

    var name: String? = "Hunger"
    var number: String? = "9988776655"
    var address: String? = "India"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = activity!!.getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        txtName = view.findViewById(R.id.txtName)
        txtNumber = view.findViewById(R.id.txtNumber)
        txtAddress = view.findViewById(R.id.txtAddress)
        btnMyOrders = view.findViewById(R.id.btnMyOrders)
        btnFavorite = view.findViewById(R.id.btnFavorite)

        btnMyOrders.setOnClickListener {
            val intent = Intent(activity as Context,OrderFragment::class.java)
            startActivity(intent)
        }

        btnFavorite.setOnClickListener {
            val intent = Intent(activity as Context,FavoritesFragment::class.java)
            startActivity(intent)
        }

        val uname = sharedPreferences.getString("HungerHalter",name)
        val unumber =  sharedPreferences.getString("9988776655",number)
        val uaddress = sharedPreferences.getString("India", address)

        txtName.text = uname
        txtNumber.text = unumber
        txtAddress.text = uaddress



        return view
    }
}