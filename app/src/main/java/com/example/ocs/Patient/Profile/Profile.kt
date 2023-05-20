package com.example.ocs.Patient.Profile

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
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.Patient.booking.BookAppointment
import com.example.ocs.Patient.services.Services
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*


class Profile : AppCompatActivity() {
    private lateinit var intent2: Intent
    private  var dataBase: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var name:TextView
    private lateinit var birthDate:TextView
    private lateinit var phone:TextView
    private lateinit var address:TextView
    private lateinit var email:TextView
    private lateinit var pref: Prefrences
    private lateinit var context: Context
    private lateinit var navHeader : View
    private lateinit var  drawerLayout : DrawerLayout
    private lateinit var  navView : NavigationView
    private lateinit var userName:TextView
    // navigation bar
    lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_patient)
        supportActionBar!!.elevation= 0F
        init()
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
        startActivity(Intent(this, BookAppointment::class.java))
    }

    private fun patientProfile() {
        startActivity(Intent(this, Profile::class.java))
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


    private fun init(){
        intent2=intent
        drawerLayout= findViewById(R.id.drawerLayout)
        navView= findViewById(R.id.nav_view)
        navHeader=navView.getHeaderView(0)
        name=findViewById(R.id.patientName)
        birthDate=findViewById(R.id.birthdate_txt)
        phone=findViewById(R.id.phone_txt)
        email=findViewById(R.id.email_txt)
        address=findViewById(R.id.address_txt)
        context=this
        pref= Prefrences(context)
        userName=navHeader.findViewById(R.id.user_name)
        userName.setText(pref.userName)
        dataBase.child("Patients").child(pref.prefID.toString()).get().addOnSuccessListener {
            if (it.exists()){
                name.text = it.child("firstName").value.toString().plus(" ").plus(it.child("lastName").value.toString())
                address.text = it.child("address").value.toString()
                birthDate.text = it.child("birthDate").value.toString()
                phone.text = it.child("phone").value.toString()
                email.text = it.child("email").value.toString()
            }
        }
    }
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
}