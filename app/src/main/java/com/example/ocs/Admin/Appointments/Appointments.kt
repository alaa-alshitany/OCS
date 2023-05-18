package com.example.ocs.Admin.Appointments

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.Admin.AdminData
import com.example.ocs.Admin.DataClass_req
import com.example.ocs.Admin.Doctors.DoctorAdapter
import com.example.ocs.Admin.Doctors.DoctorData
import com.example.ocs.Patient.booking.AppointmentData
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
    private lateinit var appAdapter :AppointmentAdapter
    private lateinit var approvedList:ArrayList<AppointmentData>
    private lateinit var requestsBtn:Button
    private lateinit var approvedBtn:Button
    private lateinit var layoutTitle:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_requests)
        supportActionBar!!.elevation=0F
        init()
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
               // R.id.nau_dashboard->dashboard()
               // R.id.nau_profile2-> adminProfile()
               // R.id.nau_booking2-> requests()
               // R.id.nau_doctor-> doctor()
               // R.id.nau_logout2-> logout()
            }
            true
        }
        requestsBtn.setOnClickListener {
            layoutTitle.setText(R.string.requestsBtn)
            getRequestsData()
        }
        approvedBtn.setOnClickListener {
            layoutTitle.setText(R.string.approvedBtn)
            getApprovedData()
        }
    }
    private fun init(){
        requestsRecycle=findViewById(R.id.appointmentRecycle)
        requestsRecycle.layoutManager=LinearLayoutManager(this)
        requestsRecycle.setHasFixedSize(true)
        requestsList= arrayListOf<AppointmentData>()
        approvedList= arrayListOf<AppointmentData>()
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        requestsBtn=findViewById(R.id.btn_req)
        approvedBtn=findViewById(R.id.btn_approved)
        layoutTitle=findViewById(R.id.approved_txtView)
    }
    private fun getRequestsData(){
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
                    appAdapter= AppointmentAdapter(requestsList)
                    requestsRecycle.adapter=appAdapter
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
                    appAdapter= AppointmentAdapter(approvedList)
                    requestsRecycle.adapter=appAdapter

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,R.string.appointmentsNotFound,Toast.LENGTH_LONG).show()

            }

        })

    }
}