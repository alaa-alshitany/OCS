package com.example.ocs.Intro.patient.booking

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ocs.Intro.patient.Profile.Profile
import com.example.ocs.Intro.patient.services.services
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class BookAppointment : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private  var database:DatabaseReference=FirebaseDatabase.getInstance().reference
    private lateinit var patientID:String
    private lateinit var intent2:Intent
    private lateinit var bookBtn: Button
    private lateinit var fullName: EditText
    private lateinit var phone:EditText
    private lateinit var rays:CheckBox
    private lateinit var tests:CheckBox
    private lateinit var clinics:CheckBox
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appoitnment)
        init()
        getExtras()
        defaultData()
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
        bookBtn.setOnClickListener {
            sendRequest()
        }
    }

    private fun defaultData() {
    database.child("Patients").child(patientID).get().addOnSuccessListener {
        if (it.exists()){
            fullName.setText(it.child("firstName").value.toString().plus(" ").plus(it.child("lastName").value.toString()))
            phone.setText(it.child("phone").value.toString())
        }
    }
    }

    private fun sendRequest() {


    }

    private fun logout() {
    }

    private fun booking() {
        startActivity(Intent(this, BookAppointment::class.java).putExtra("patientID",patientID))
    }

    private fun patientProfile() {
        startActivity(Intent(this, Profile::class.java).putExtra("patientID",patientID))
    }

    private fun home() {
        startActivity(Intent(this, services::class.java))
    }
    //nav_bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun init(){
        intent2=intent
        bookBtn=findViewById(R.id.sendRequestBtn)
        rays=findViewById(R.id.medical_Rays)
        tests=findViewById(R.id.medical_tests)
        clinics=findViewById(R.id.medical_clinics)
        phone =findViewById(R.id.phone_edt)
        fullName=findViewById(R.id.fullName_edt)
    }
    private fun getExtras(){
        patientID=intent2.getStringExtra("patientID").toString()
    }
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

}