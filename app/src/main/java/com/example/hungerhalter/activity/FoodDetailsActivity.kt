package com.example.hungerhalter.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.hungerhalter.R
import com.example.hungerhalter.adapter.RecyclerDetailAdapter
import com.example.hungerhalter.model.Food
import com.example.hungerhalter.util.ConnectionManager
import org.json.JSONException

class FoodDetailsActivity : AppCompatActivity() {
    lateinit var recyclerDetails: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: RecyclerDetailAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var toolbar: Toolbar
    var restaurantId: String? = "100"
    var restaurantName: String? = "Baco Tell"

    var foodList = arrayListOf<Food>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)

        recyclerDetails = findViewById(R.id.recyclerDetails)
        layoutManager = LinearLayoutManager(this@FoodDetailsActivity)

        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE


        if(intent != null) {
            restaurantId = intent.getStringExtra("restaurantId")
            restaurantName = intent.getStringExtra("restaurantName")
        } else {
            finish()
            Toast.makeText(this@FoodDetailsActivity,"Some unexpected error occurred!",Toast.LENGTH_SHORT).show()
        }

        if(restaurantId == "100") {
            finish()
            Toast.makeText(this@FoodDetailsActivity,"Some unexpected error occurred!",Toast.LENGTH_SHORT).show()
        }

        setupToolbar()

        val queue = Volley.newRequestQueue(this@FoodDetailsActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/${restaurantId}"

        if(ConnectionManager().checkConnectivity(this@FoodDetailsActivity)) {
            val jsonObjectRequest = object: JsonObjectRequest(Method.GET,url,null, Response.Listener{
                try{
                    progressLayout.visibility = View.GONE
                    val json = it.getJSONObject("data")
                    val success = json.getBoolean("success")

                    if(success) {
                        val data = json.getJSONArray("data")
                        for(i in 0 until data.length()) {
                            val foodJsonObject = data.getJSONObject(i)
                            val foodObject = Food(
                                    foodJsonObject.getString("id"),
                                    foodJsonObject.getString("name"),
                                    "Rs. ${foodJsonObject.getString("cost_for_one")}",
                                    foodJsonObject.getString("restaurant_id")
                            )

                            foodList.add(foodObject)
                            recyclerAdapter = RecyclerDetailAdapter(this@FoodDetailsActivity,foodList)
                            recyclerDetails.adapter = recyclerAdapter
                            recyclerDetails.layoutManager = layoutManager
                        }
                    } else {
                        Toast.makeText(this@FoodDetailsActivity,"Some Error Occurred!!",Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this@FoodDetailsActivity,"Some unexpected Error Occurred!!",Toast.LENGTH_SHORT).show()
                }
            },Response.ErrorListener {
                Toast.makeText(this@FoodDetailsActivity,"Some unexpected Error Occurred!!!",Toast.LENGTH_SHORT).show()
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "2b48362961bdf5"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(this@FoodDetailsActivity)
            dialog.setTitle("Failed")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
        }
    }

    fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = restaurantName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}