package com.example.ocs.Doctor.Model

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
import com.example.ocs.Doctor.Appointments.Appointments
import com.example.ocs.Doctor.DoctorProfile.Profile
import com.example.ocs.Doctor.Patients
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.R
import com.example.ocs.databinding.ActivityPreModelBinding
import com.google.android.material.navigation.NavigationView

class Pre_model : AppCompatActivity() {

    private lateinit var pref: Prefrences
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var context: Context
    private lateinit var navHeader : View
    private lateinit var userName: TextView
    //nav_bar
    lateinit var toggle: ActionBarDrawerToggle

    private lateinit var binding: ActivityPreModelBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_model)
        supportActionBar!!.elevation=0F
        init()

        binding = ActivityPreModelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.elevation= 0F
        binding.predict?.setOnClickListener {
            startActivity(Intent(this,Predict::class.java))
        }
        binding.upload?.setOnClickListener {
            startActivity(Intent(this,UploadFiles::class.java))
        }

        //nav_bar
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nau_profile1-> doctorProfile()
                R.id.nau_booking1-> appointment()
                R.id.nau_patient-> patients()
                R.id.nau_model-> model()
                R.id.nau_logout1-> logout()
            }

            true
        }

    }

    //nav_bar
    private fun logout() {
        pref.prefClear()
        moveToLogin()
    }
    private fun moveToLogin() {
        startActivity(Intent(this, Login::class.java).putExtra("hint",R.string.p_email_hint.toString()))
        Toast.makeText(this,R.string.logout, Toast.LENGTH_LONG).show()
        finish()
    }
    private fun appointment() {
        startActivity(Intent(this, Appointments::class.java))
    }

    private fun doctorProfile() {
        startActivity(Intent(this, Profile::class.java))
    }
    private fun patients() {
        startActivity(Intent(this, Patients::class.java))
    }

    private fun model() {
        startActivity(Intent(this, Pre_model::class.java))
    }

    //nav_bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

    private fun init(){
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        navHeader=navView.getHeaderView(0)
        context=this
        pref= Prefrences(context)
        userName=navHeader.findViewById(R.id.user_name)
        userName.setText(pref.userName)

    }
}