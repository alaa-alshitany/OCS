package com.example.ocs.Admin.Appointments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.Admin.Dashboard.Dashboard
import com.example.ocs.Admin.Doctors.DoctorData
import com.example.ocs.Admin.Doctors.Doctors
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.Patient.Profile.Profile
import com.example.ocs.Patient.booking.AppointmentData
import com.example.ocs.Patient.booking.BookAppointment
import com.example.ocs.Patient.services.Services
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class Appointments : AppCompatActivity()  {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var requestsRecycle: RecyclerView
    private lateinit var requestsList: ArrayList<AppointmentData>
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var requestsAdapter :RequestsAdapter
    private lateinit var approvedAdapter: ApprovedAdapter
    private lateinit var approvedList:ArrayList<AppointmentData>
    private lateinit var requestsBtn:Button
    private lateinit var approvedBtn:Button
    private lateinit var layoutTitle:TextView
    private lateinit var context: Context
    private lateinit var doctorList: MutableMap<String,String>
    private lateinit var pref: Prefrences
    private lateinit var navHeader : View
    private lateinit var userName:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_requests)
        supportActionBar!!.elevation=0F
        init()
        getDoctorList()
        getRequestsData()
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
        requestsBtn.setOnClickListener {
            //Toast.makeText(context,doctorList.size,Toast.LENGTH_LONG).show()
            layoutTitle.setText(R.string.requestsBtn)
            getRequestsData()
        }
        approvedBtn.setOnClickListener {
            layoutTitle.setText(R.string.approvedBtn)
            getApprovedData()
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
    private fun init(){
        requestsRecycle=findViewById(R.id.appointmentRecycle)
        requestsRecycle.layoutManager=LinearLayoutManager(this)
        requestsRecycle.setHasFixedSize(true)
        requestsList= arrayListOf<AppointmentData>()
        approvedList= arrayListOf<AppointmentData>()
        doctorList= mutableMapOf<String,String>()
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        navHeader=navView.getHeaderView(0)
        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        requestsBtn=findViewById(R.id.btn_req)
        approvedBtn=findViewById(R.id.btn_approved)
        layoutTitle=findViewById(R.id.approved_txtView)
        context=this
        pref= Prefrences(context)
        userName=navHeader.findViewById(R.id.user_name)
        userName.setText(pref.userName)
    }
    private fun getDoctorList(){
        database.child("Doctors").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (docData in snapshot.children) {
                        val doctor = docData.getValue<DoctorData>()
                        var doctorName ="DR/ ${doctor!!.firstName} ${doctor!!.lastName}"
                        var doctorID="${doctor!!.id}"
                        doctorList.put(doctorName,doctorID)
                    }
                }else{
                    Toast.makeText(applicationContext,"canceled", Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun getRequestsData(){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS),1)
        }
        database.child("Appointments").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                requestsList.clear()
                if (snapshot.exists()){
                    for (appData in snapshot.children){
                        var appointment = appData.getValue<AppointmentData>()
                        if(appointment?.status.equals("unApproved")){
                            //val patientName=appData.getValue(AppointmentData::class.java)
                            requestsList.add(appointment!!)
                        }
                    }
                    requestsAdapter= RequestsAdapter(context,requestsList,doctorList)
                    requestsRecycle.adapter=requestsAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,R.string.appointmentsNotFound,Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun getApprovedData(){
        database.child("Appointments").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                approvedList.clear()
                if (snapshot.exists()){
                    for (appData in snapshot.children){
                        var appointment = appData.getValue<AppointmentData>()
                        if(appointment?.status.equals("Approved")){
                            approvedList.add(appointment!!)
                        }
                    }
                    approvedAdapter= ApprovedAdapter(context,approvedList,doctorList)
                    requestsRecycle.adapter=approvedAdapter

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,R.string.appointmentsNotFound,Toast.LENGTH_LONG).show()

            }

        })

    }
}