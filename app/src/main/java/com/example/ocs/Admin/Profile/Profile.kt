package com.example.ocs.Admin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ocs.Admin.Appointments.Appointments
import com.example.ocs.R
import com.example.ocs.Admin.Doctors.Doctors
import com.example.ocs.Patient.services.ServiceData
import com.example.ocs.Admin.Dashboard.Dashboard
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.Patient.Profile.Profile
import com.example.ocs.Patient.services.OnCardItemClickListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Profile : AppCompatActivity(), OnCardItemClickListener {
    private lateinit var adminName: TextView
    private lateinit var adminCode: TextView
    private lateinit var adminEmail: TextView
    private lateinit var adminPhone:TextView
    private  var dataBase: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var pref: Prefrences
    private lateinit var context:Context
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var navHeader : View
    private lateinit var userName:TextView
    private lateinit var progress: ProgressBar

    //nav_bar
    lateinit var toggle: ActionBarDrawerToggle
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        supportActionBar!!.elevation= 0F
        init()
        //nav_bar
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nau_dashboard->dashboard()
                R.id.nau_profile2-> adminProfile()
                R.id.nau_booking2-> requests()
                R.id.nau_doctor-> doctor()
                R.id.nau_logout2-> logout()
            }

            true
        }

    }

    private fun getAdminData() {
        progress.visibility=View.VISIBLE
        dataBase.child("Admins").child(pref.prefID.toString()).get().addOnSuccessListener {
            if (it.exists()){
                adminName.text = it.child("name").value.toString()
                adminCode.text = it.child("code").value.toString()
                adminPhone.text = it.child("phone").value.toString()
                adminEmail.text = it.child("email").value.toString()
                progress.visibility=View.GONE
            }
        }
    }
    //nav_bar
    private fun logout() {
        pref.prefClear()
        moveToLogin()
    }
    private fun moveToLogin() {
        startActivity(Intent(this, Login::class.java).putExtra("hint",R.string.a_email_hint.toString()))
        Toast.makeText(this,R.string.logout, Toast.LENGTH_LONG).show()
        finish()
    }
    private fun requests() {
        startActivity(Intent(this, Appointments::class.java))
    }

    private fun adminProfile() {
        startActivity(Intent(this, Profile::class.java))
    }

    private fun doctor() {
        startActivity(Intent(this, Doctors::class.java))
    }
    private fun dashboard() {
        startActivity(Intent(this, Dashboard::class.java))
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
    //listener
    override fun onClick(c: ServiceData?) {
        val toast = Toast.makeText(applicationContext, c?.serviceImage!!, Toast.LENGTH_LONG)
        toast.show()
    }
    private fun init(){
        context=this
        pref= Prefrences(context)
        adminEmail=findViewById(R.id.adminEmailTxt)
        adminName=findViewById(R.id.adminNameTxt)
        adminPhone=findViewById(R.id.adminPhoneTxt)
        adminCode=findViewById(R.id.adminCodeTxt)
        drawerLayout= findViewById(R.id.drawerLayout)
        navView= findViewById(R.id.nav_view)
        navHeader=navView.getHeaderView(0)
        userName=navHeader.findViewById(R.id.user_name)
        userName.setText(pref.userName)
        progress=findViewById(R.id.progress_bar)
        getAdminData()
    }
}