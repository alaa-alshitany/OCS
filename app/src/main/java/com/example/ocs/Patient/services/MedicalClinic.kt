package com.example.ocs.Patient.services

import android.annotation.SuppressLint
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
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.Patient.Profile.Profile
import com.example.ocs.Patient.booking.BookAppointment
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView

class MedicalClinic : AppCompatActivity(), OnCardItemClickListener {
    private lateinit var recycleView: RecyclerView
    private lateinit var dataList: ArrayList<ServiceData>
    lateinit var serviceList:Array<Int>
    lateinit var priceList:Array<Int>
    private lateinit var pref: Prefrences
    private lateinit var context: Context
    private lateinit var navigationView:NavigationView
    private lateinit var navHeader : View
    private lateinit var userName: TextView

    // navigation bar
    lateinit var toggle: ActionBarDrawerToggle
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_details_layout)
        supportActionBar!!.elevation= 0F
        init()
         getData()

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

    }
    private fun moveToLogin() {
        startActivity(Intent(this, Login::class.java).putExtra("hint",R.string.p_email_hint.toString()))
        Toast.makeText(this,R.string.logout, Toast.LENGTH_LONG).show()
        finish()
    }
    private fun booking() {
        startActivity(Intent(this, BookAppointment::class.java))
    }

    private fun patientProfile() {
        startActivity(Intent(this, Profile::class.java))
    }

    private fun home() {
        startActivity(Intent(this, Services::class.java))
    }
    //lisener
    override fun onClick(c: ServiceData?) {
        val toast = Toast.makeText(applicationContext, c?.serviceImage!!, Toast.LENGTH_LONG)
        toast.show()
    }
private fun init(){
    context=this
    pref= Prefrences(context)
    navigationView=findViewById(R.id.nav_view)
    navHeader=navigationView.getHeaderView(0)
    userName=navHeader.findViewById(R.id.user_name)
    userName.setText(pref.userName)
    serviceList = arrayOf(R.string.medicalClinicEA,R.string.medicalClinicWM,R.string.medicalClinicHM,R.string.medicalClinicMA)
    priceList = arrayOf(R.string.medicalClinicEAT,R.string.medicalClinicWMT,R.string.medicalClinicHMT,R.string.medicalClinicMAT)
    recycleView = findViewById(R.id.recycleView)
    recycleView.layoutManager = LinearLayoutManager(this)
    recycleView.setHasFixedSize(true)
    dataList = arrayListOf<ServiceData>()
}

    //nav_bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getData(){
        for(i in serviceList.indices){
            val dataClass = ServiceData(0,serviceList[i], priceList[i])
            dataList.add(dataClass)
        }
        recycleView.adapter = ServiceDetailsAdapter(dataList)
    }

}