package com.example.ocs.Doctor.Model

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ocs.Doctor.Appointments.Appointments
import com.example.ocs.Doctor.DoctorProfile.Profile
import com.example.ocs.Doctor.Patients
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Patient.PatientData
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage

class Predict: AppCompatActivity() {
    private lateinit var patientSpinner:Spinner
    private lateinit var drugSpinner:Spinner
    val database = FirebaseDatabase.getInstance().reference
    val storageRef = FirebaseStorage.getInstance().getReference("Test_Data/")
    private lateinit var adapter:ArrayAdapter<String>
    //nav_bar
    lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.predict)

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

    }

    private fun moveToLogin() {
        startActivity(Intent(this, Login::class.java).putExtra("hint",R.string.d_email_hint.toString()))
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
        startActivity(Intent(this, Predict::class.java))
    }
    //nav_bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun init(){
        patientSpinner=findViewById(R.id.patientsSpinner)
        drugSpinner=findViewById(R.id.drugsSpinner)
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        patientSpinner.adapter = adapter
        database.child("Patients").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.clear()
                if (snapshot.exists()){
                    for (patientData in snapshot.children){
                        var patient = patientData.getValue<PatientData>()
                        val patientName="${patient?.firstName} ${patient?.lastName}"
                        adapter.add(patientName)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        drugSpinner=findViewById(R.id.drugsSpinner)
        ArrayAdapter.createFromResource(this,R.array.drugNames,android.R.layout.simple_spinner_item).also {
                adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            drugSpinner.adapter=adapter
        }
    }
}
