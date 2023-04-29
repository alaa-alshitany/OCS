package com.example.ocs.Intro.patient.Profile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ocs.Intro.Login.PatientData
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class Profile : AppCompatActivity() {
    private lateinit var intent2: Intent
    private lateinit var patientID:String
    private  var dataBase: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var name:TextView
    private lateinit var birthDate:TextView
    private lateinit var phone:TextView
    private lateinit var address:TextView
    private lateinit var email:TextView

    // navigation bar
    lateinit var toggle: ActionBarDrawerToggle


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_patient)

        init()
        getExtras()
        getDataDB()

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


    private fun init(){
        intent2=intent
        name=findViewById(R.id.patientName)
        birthDate=findViewById(R.id.birthdate_txt)
        phone=findViewById(R.id.phone_txt)
        email=findViewById(R.id.email_txt)
        address=findViewById(R.id.address_txt)
    }
    private fun getDataDB(){
        dataBase.child("Patients").child(patientID).get().addOnSuccessListener {
            if (it.exists()){
                name.text = it.child("firstName").value.toString().plus(" ").plus(it.child("lastName").value.toString())
                address.text = it.child("address").value.toString()
                birthDate.text = it.child("birthDate").value.toString()
                phone.text = it.child("phone").value.toString()
                email.text = it.child("email").value.toString()
            }
                    }
    }
    private fun getExtras(){
        patientID=intent2.getStringExtra("patientID").toString()
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
}