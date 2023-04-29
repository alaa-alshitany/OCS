package com.example.ocs.Intro.patient.services

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView

class services : AppCompatActivity() {
    private lateinit var intent2: Intent
    private lateinit var fullName:String
    private lateinit var list: RecyclerView
    // navigation bar
    lateinit var toggle: ActionBarDrawerToggle
    private fun init() {
       list=findViewById(R.id.recyclerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        init()
        // this creates a vertical layout Manager
        list.layoutManager=LinearLayoutManager(this)

        // ArrayList of class serviceViewModel
        val data = ArrayList<serviceModel>(4)
        data.add(serviceModel(R.drawable.rays_service,R.string.slider2_title))
        data.add(serviceModel(R.drawable.booking,R.string.book_appointment_title))
        data.add(serviceModel(R.drawable.tests_service,R.string.slider3_title))
        data.add(serviceModel(R.drawable.clinics_service,R.string.clinic_service))


        //This will pass the ArrayList to our Adapter
        val adapter = serviceAdapter(this,data)

        // Setting the Adapter with the recyclerview
        list.adapter = adapter
        getIntentExtra()
        //adapter.setOnItem
        //navigation bar
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nau_home-> Toast.makeText(applicationContext,"clicked Home", Toast.LENGTH_SHORT).show()
                R.id.nau_profile-> Toast.makeText(applicationContext,"clicked Profile", Toast.LENGTH_SHORT).show()
                R.id.nau_booking-> Toast.makeText(applicationContext,"clicked Booking", Toast.LENGTH_SHORT).show()
                R.id.nau_logout-> Toast.makeText(applicationContext,"clicked Logout", Toast.LENGTH_SHORT).show()
            }

            true
        }


    }
    //nav_bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getIntentExtra(){
        intent2=intent
        fullName=intent2.getStringExtra("name").toString()
    }

}