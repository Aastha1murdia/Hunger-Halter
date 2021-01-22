package com.example.hungerhalter.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hungerhalter.R
import com.example.hungerhalter.fragment.*
import com.google.android.material.navigation.NavigationView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frames: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences

    lateinit var txtNumber: TextView
    lateinit var txtName: TextView
    lateinit var txtAddress: TextView


    var Name: String? = "Hunger Halter"
    var Mobile: String? = "9988776655"
    var Address: String? = "India"

    var previousMenuItem: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinateLayout)
        toolbar = findViewById(R.id.toolbar)
        frames = findViewById(R.id.frames)
        navigationView = findViewById(R.id.navigationView)
        txtName = navigationView.getHeaderView(0).findViewById(R.id.txtName)
        txtNumber = navigationView.getHeaderView(0).findViewById(R.id.txtNumber)
        txtAddress = navigationView.getHeaderView(0).findViewById(R.id.txtAddress)

        setupToolbar()
        homepage()

        Name = sharedPreferences.getString("HungerHalter","HungerHalter")
        Mobile = sharedPreferences.getString("9988776655","9988776655")
        Address = sharedPreferences.getString("India","India")

        txtName.text = Name
        txtNumber.text = Mobile
        txtAddress.text = Address


        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout,R.string.open_drawer,R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.drawerArrowDrawable.setColor(resources.getColor(R.color.colorAccent))
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    homepage()
                    drawerLayout.closeDrawers()
                }

                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frames,ProfileFragment()).commit()
                    supportActionBar?.title = "My Profile"
                    drawerLayout.closeDrawers()
                }

                R.id.favorites -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frames,FavoritesFragment()).commit()
                    supportActionBar?.title = "All Favorites Restaurant"
                    drawerLayout.closeDrawers()
                }

                R.id.order_history -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frames,OrderFragment()).commit()
                    supportActionBar?.title = "My Order Details"
                    drawerLayout.closeDrawers()
                }

                R.id.faq -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frames, HelpFragment()).commit()
                    supportActionBar?.title = "Frequently Asked Questions (FAQs)"
                    drawerLayout.closeDrawers()
                }


                R.id.logout -> {
                    val logout = AlertDialog.Builder(this@MainActivity)
                    logout.setTitle("Logout")
                    logout.setPositiveButton("Ok"){ text, listener ->
                        sharedPreferences.edit().clear()
                        sharedPreferences.edit().commit()
                        var intent = Intent(this@MainActivity,LoginActivity::class.java)
                        startActivity(intent)

                    }
                }
            }

            if(previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

            it.isChecked = true
            it.isCheckable = true
            previousMenuItem = it

            return@setNavigationItemSelectedListener true
        }

    }

    fun setupToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Hunger Halter"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    fun homepage(){   
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frames,fragment)
        transaction.commit()
        supportActionBar?.title = "All Restaurant Details"

        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frames)

        when(frag){
            !is HomeFragment -> homepage()
            else -> super.onBackPressed()
        }
    }
}