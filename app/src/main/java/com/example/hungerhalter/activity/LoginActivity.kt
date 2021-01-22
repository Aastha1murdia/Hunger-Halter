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
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var etMobileNumber: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtRegister: TextView
    lateinit var sharedPreferences: SharedPreferences


    var userList = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        setContentView(R.layout.activity_login)

        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val inputNumber = etMobileNumber.text.toString()
            val inputPassword = etPassword.text.toString()

            val queue = Volley.newRequestQueue(this@LoginActivity)
            val url = "http://13.235.250.119/v2/login/fetch_result"

            val jsonParam = JSONObject()

            jsonParam.put("mobile_number", inputNumber)
            jsonParam.put("password", inputPassword)

            if (ConnectionManager().checkConnectivity(this@LoginActivity)) {
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

                                var name = userObject.username
                                var email = userObject.useremail
                                var number = userObject.usermobile
                                var address = userObject.useraddress

                                savePreferences(name,email,number,address)


                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Some Error Occurred!!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(this@LoginActivity, "Some unexpected Error Occurred!!", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@LoginActivity, "Some unexpected Error Occurred!!!", Toast.LENGTH_SHORT).show()
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
                val dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("Failed")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
            }

            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
        }

        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        txtRegister = findViewById(R.id.txtRegister)
        txtRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun savePreferences(name: String, email: String, number: String, address: String){
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
        sharedPreferences.edit().putString("HungerHalter", name).apply()
        sharedPreferences.edit().putString("hunger@example.com", email).apply()
        sharedPreferences.edit().putString("9988776655", number).apply()
        sharedPreferences.edit().putString("India", address).apply()

    }
}
