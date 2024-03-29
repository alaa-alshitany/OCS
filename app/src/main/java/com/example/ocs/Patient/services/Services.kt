package com.example.ocs.Patient.services

import android.content.Context
import android.content.Intent
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
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Patient.Profile.Profile
import com.example.ocs.Patient.booking.BookAppointment
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView

class Services : AppCompatActivity(), OnCardItemClickListener {
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
        supportActionBar!!.elevation= 0F
        init()
        // this creates a vertical layout Manager
        list.layoutManager=LinearLayoutManager(this)

        // ArrayList of class serviceViewModel
        val data = ArrayList<ServiceData>(4)
        data.add(ServiceData(R.drawable.rays_service,R.string.slider2_title,0))
        data.add(ServiceData(R.drawable.booking,R.string.book_appointment_title,0))
        data.add(ServiceData(R.drawable.tests_service,R.string.slider3_title,0))
        data.add(ServiceData(R.drawable.clinics_service,R.string.clinic_service,0))


        //This will pass the ArrayList to our Adapter
        val adapter = ServiceAdapter(this,data,this)

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
        startActivity(Intent(this, Login::class.java).putExtra("hint",R.string.p_email_hint.toString()))
        Toast.makeText(this,R.string.logout, Toast.LENGTH_LONG).show()
        finish()
    }
    private fun booking() {
        startActivity(Intent(this, BookAppointment::class.java).putExtra("patientID",patientID))
    }

    private fun patientProfile() {
        startActivity(Intent(this, Profile::class.java).putExtra("patientID",patientID))
    }

    private fun home() {
        startActivity(Intent(this, Services::class.java))
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

    override fun onClick(c: ServiceData?) {
        if (c?.serviceImage!!.equals(R.drawable.rays_service)) {
            val doneIntent = Intent(this, MedicalRays::class.java)
            startActivity(doneIntent) }
        else if (c?.serviceImage!!.equals(R.drawable.booking)) {
            val doneIntent = Intent(this, BookAppointment::class.java)
            startActivity(doneIntent) }
        else if (c?.serviceImage!!.equals(R.drawable.tests_service)){
            val doneIntent = Intent(this, MedicalTests::class.java)
            startActivity(doneIntent) }
        else if (c?.serviceImage!!.equals(R.drawable.clinics_service)){
            val doneIntent = Intent(this, MedicalClinic::class.java)
            startActivity(doneIntent) }
    }

}