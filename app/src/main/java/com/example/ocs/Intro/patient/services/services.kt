package com.example.ocs.Intro.patient.services

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.Intro.Login.Prefrences
import com.example.ocs.Intro.Login.login
import com.example.ocs.Intro.patient.Profile.Profile
import com.example.ocs.Intro.patient.booking.BookAppointment
import com.example.ocs.R
import com.example.ocs.profile.medical_clinic
import com.example.ocs.profile.medical_rays
import com.example.ocs.profile.medical_tests
import com.google.android.material.navigation.NavigationView

class services : AppCompatActivity(), OnCardItemClickListener {
    private lateinit var intent2: Intent
    private lateinit var list: RecyclerView
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var patientID:String
    private lateinit var pref: Prefrences
    private lateinit var context: Context
    private lateinit var navigationView:NavigationView
    private lateinit var navHeader :View
    private lateinit var userName: TextView
    private fun init() {
       list=findViewById(R.id.recyclerView)
        context=this
        pref= Prefrences(context)
        navigationView=findViewById(R.id.nav_view)
        navHeader=navigationView.getHeaderView(0)
       userName=navHeader.findViewById(R.id.user_name)
        userName.setText(pref.userName)
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
        val adapter = serviceAdapter(this,data,this)

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
                R.id.nau_home-> home()
                R.id.nau_profile-> patientProfile()
                R.id.nau_booking-> booking()
                R.id.nau_logout-> logout()
            }
            true
        }
    }

    private fun logout() {
        pref.prefClear()
        moveToLogin()
    }
    private fun moveToLogin() {
        startActivity(Intent(this, login::class.java).putExtra("hint",R.string.p_email_hint.toString()))
        Toast.makeText(this,R.string.logout, Toast.LENGTH_LONG).show()
        finish()
    }
    private fun booking() {
        startActivity(Intent(this,BookAppointment::class.java).putExtra("patientID",patientID))
    }

    private fun patientProfile() {
        startActivity(Intent(this, Profile::class.java).putExtra("patientID",patientID))
    }

    private fun home() {
        startActivity(Intent(this,services::class.java))
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
        patientID=intent2.getStringExtra("patientID").toString()
    }

    override fun onClick(c: serviceModel) {
        if (c?.serviceImage!!.equals(R.drawable.rays_service)) {
            val doneIntent = Intent(this, medical_rays::class.java)
            startActivity(doneIntent) }
        else if (c?.serviceImage!!.equals(R.drawable.booking)) {
            val doneIntent = Intent(this, booking()::class.java)
            startActivity(doneIntent) }
        else if (c?.serviceImage!!.equals(R.drawable.tests_service)){
            val doneIntent = Intent(this, medical_tests::class.java)
            startActivity(doneIntent) }
        else if (c?.serviceImage!!.equals(R.drawable.clinics_service)){
            val doneIntent = Intent(this, medical_clinic::class.java)
            startActivity(doneIntent) }
    }

}