package com.example.ocs.Doctor.Appointments

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.Admin.Appointments.ApprovedAdapter
import com.example.ocs.Doctor.DoctorProfile.Profile
import com.example.ocs.Doctor.Model.Pre_model
import com.example.ocs.Doctor.Patients
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.Patient.booking.AppointmentData
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class Appointments : AppCompatActivity() {
    private lateinit var recycleView: RecyclerView
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var approvedAdapter: ApprovedAdapter
    private lateinit var approvedList:ArrayList<AppointmentData>
    private lateinit var context: Context
    private lateinit var doctorList: MutableMap<String,String>
    private lateinit var pref: Prefrences
    private lateinit var navHeader : View
    private lateinit var userName: TextView
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var progress: ProgressBar


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_book)
        supportActionBar!!.elevation=0F
        init()
        getApprovedData()
        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
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
        approvedList= arrayListOf<AppointmentData>()
        recycleView = findViewById(R.id.recycleView)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)
        navHeader=navView.getHeaderView(0)
        context=this
        doctorList= mutableMapOf<String,String>()
        pref= Prefrences(context)
        userName=navHeader.findViewById(R.id.user_name)
        userName.setText(pref.userName)
        progress=findViewById(R.id.progress_bar)
    }
    private fun getApprovedData(){
        database.child("Appointments").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                approvedList.clear()
                if (snapshot.exists()){
                    for (appData in snapshot.children){
                        var appointment = appData.getValue<AppointmentData>()
                        if(appointment?.status.equals("Approved")){
                            if (appointment?.doctorID.equals(pref.prefID)){
                                approvedList.add(appointment!!)
                            }
                        }
                    }
                    approvedAdapter= ApprovedAdapter(context,approvedList,doctorList)
                    recycleView.adapter=approvedAdapter

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,R.string.appointmentsNotFound,Toast.LENGTH_LONG).show()

            }

        })

    }

}
