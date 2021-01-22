package com.example.hungerhalter.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.hungerhalter.R
import com.example.hungerhalter.model.User
import com.example.hungerhalter.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etEmailAddress: EditText
    lateinit var etMobileNumber: EditText
    lateinit var etDeliveryAddress: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnRegister: Button
    lateinit var sharedPreferences: SharedPreferences

    var userList = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        setContentView(R.layout.activity_register)


        etName = findViewById(R.id.etName)
        etEmailAddress = findViewById(R.id.etEmailAddress)
        etMobileNumber = findViewById(R.id.etMobileNumber)
        etDeliveryAddress = findViewById(R.id.etDeliveryAddress)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)



        btnRegister.setOnClickListener {

            val name = etName.text.toString()
            val email = etEmailAddress.text.toString()
            val mobile = etMobileNumber.text.toString()
            val address = etDeliveryAddress.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (name.isNotEmpty()) {
                if (email.isNotEmpty()) {
                    if (mobile.length == 10 && mobile.isNotEmpty()) {
                        if (address.isNotEmpty()) {
                            if (password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                                if (password.length == confirmPassword.length) {

                                    val queue = Volley.newRequestQueue(this@RegisterActivity)
                                    val url = "http://13.235.250.119/v2/register/fetch_result"

                                    val jsonParam = JSONObject()
                                    jsonParam.put("name", name)
                                    jsonParam.put("mobile_number", mobile)
                                    jsonParam.put("password", password)
                                    jsonParam.put("address", address)
                                    jsonParam.put("email", email)

                                    savePreferences(name,email,mobile,address)

                                    if (ConnectionManager().checkConnectivity(this@RegisterActivity)) {
                                        val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParam, Response.Listener {
                                            try {
                                                val json = it.getJSONObject("data")
                                                val success = json.getBoolean("success")

                                                if (success) {
                                                    val data = json.getJSONArray("data")
                                                    for (i in 0 until data.length()) {
                                                        val userJsonObject = data.getJSONObject(i)
                                                        val userObject = User(
                                                                userJsonObject.getString("user_id"),
                                                                userJsonObject.getString("name"),
                                                                userJsonObject.getString("email"),
                                                                userJsonObject.getString("mobile_number"),
                                                                userJsonObject.getString("address")
                                                        )
                                                        userList.add(userObject)
                                                    }
                                                } else {
                                                    Toast.makeText(this@RegisterActivity, "Some Error Occurred!!", Toast.LENGTH_SHORT).show()
                                                }
                                            } catch (e: JSONException) {
                                                Toast.makeText(this@RegisterActivity, "Some unexpected Error Occurred!!", Toast.LENGTH_SHORT).show()
                                            }
                                        }, Response.ErrorListener {
                                            Toast.makeText(this@RegisterActivity, "Some unexpected Error Occurred!!!", Toast.LENGTH_SHORT).show()
                                        }) {
                                            override fun getHeaders(): MutableMap<String, String> {
                                                val headers = HashMap<String, String>()
                                                headers["Content-type"] = "application/json"
                                                headers["token"] = "2b48362961bdf5"
                                                return headers
                                            }
                                        }

                                        queue.add(jsonRequest)
                                    } else {
                                        val dialog = AlertDialog.Builder(this@RegisterActivity)
                                        dialog.setTitle("Failed")
                                        dialog.setMessage("Internet Connection Not Found")
                                        dialog.setPositiveButton("Open Settings") { text, listener ->
                                            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                                            startActivity(settingsIntent)
                                            finish()
                                        }
                                    }

                                    val intent = Intent(this@RegisterActivity,MainActivity::class.java)
                                    startActivity(intent)

                                } else {
                                    Toast.makeText(this@RegisterActivity,"Password not match",Toast.LENGTH_SHORT)
                                }
                            } else {
                                Toast.makeText(this@RegisterActivity,"Password & confirm password should not be blank",Toast.LENGTH_SHORT)
                            }
                        } else {
                            Toast.makeText(this@RegisterActivity,"Address should not be blank",Toast.LENGTH_SHORT)
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity,"Mobile number should be 10 digits",Toast.LENGTH_SHORT)
                    }
                } else {
                    Toast.makeText(this@RegisterActivity,"Email should not be blank",Toast.LENGTH_SHORT)
                }
            } else {
                Toast.makeText(this@RegisterActivity,"Name should not be blank",Toast.LENGTH_SHORT)
            }
        }
    }

    fun savePreferences(name: String, email: String, number: String, address: String){
        sharedPreferences.edit().putBoolean("isRegistered", true).apply()
        sharedPreferences.edit().putString("HungerHalter", name).apply()
        sharedPreferences.edit().putString("hunger@example.com", email).apply()
        sharedPreferences.edit().putString("9988776655", number).apply()
        sharedPreferences.edit().putString("India", address).apply()

    }
}