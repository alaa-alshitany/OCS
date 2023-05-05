package com.example.ocs.admin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R
import com.example.ocs.patient.services.OnItemRecycleClickListener
import com.example.ocs.patient.services.serviceModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Doctors : AppCompatActivity() , OnItemRecycleClickListener {
    private lateinit var doctorRecycle: RecyclerView
    private lateinit var doctorList: ArrayList<DoctorData>
    private lateinit var searchView:SearchView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView : NavigationView
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var addDoctorBtn:Button
    private lateinit var auth: FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_doctor)
        init()
        getDoctorsData()
        addDoctorBtn.setOnClickListener { addDoctorDialog() }
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nau_profile2-> Toast.makeText(applicationContext,"clicked Profile", Toast.LENGTH_SHORT).show()
                R.id.nau_booking2-> Toast.makeText(applicationContext,"clicked Booking", Toast.LENGTH_SHORT).show()
                R.id.nau_doctor-> Toast.makeText(applicationContext,"clicked doctors", Toast.LENGTH_SHORT).show()
                R.id.nau_logout2-> Toast.makeText(applicationContext,"clicked Logout", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun addDoctorDialog() {
        val dialogView =LayoutInflater.from(this).inflate(R.layout.service_item,null)
        val builder =AlertDialog.Builder(this).setView(dialogView).setTitle("Add New Doctor")
        val alertDialog=builder.show()

    }

    private fun init(){
        doctorRecycle = findViewById(R.id.recycleview1)
        doctorRecycle.layoutManager = LinearLayoutManager(this)
        doctorRecycle.setHasFixedSize(true)
        doctorList = arrayListOf<DoctorData>()
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        searchView=findViewById(R.id.searchDoctor)
        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addDoctorBtn=findViewById(R.id.addingDoctorBtn)
}
    //nav_bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //lisener
    override fun onClick(c: serviceModel?) {
        val toast = Toast.makeText(applicationContext, c?.serviceImage!!, Toast.LENGTH_LONG)
        toast.show()
    }

private fun getDoctorsData(){
    database.child("Doctors").addValueEventListener(object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                for (docData in snapshot.children){
                    val docName=docData.getValue(DoctorData::class.java)
                    doctorList.add(docName!!)
                }
                val dAdapter=DoctorAdapter(doctorList)
                doctorRecycle.adapter=dAdapter
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })

}

}