package com.example.ocs.Patient.booking

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.Patient.Profile.Profile
import com.example.ocs.Patient.services.Services
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class BookAppointment : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private  var database:DatabaseReference=FirebaseDatabase.getInstance().reference
    private var database2:DatabaseReference=FirebaseDatabase.getInstance().getReference("Appointments")
    private lateinit var intent2:Intent
    private lateinit var bookBtn: Button
    private lateinit var fullName: EditText
    private lateinit var pref: Prefrences
    private lateinit var context: Context
    private lateinit var phone:EditText
    private lateinit var rays:CheckBox
    private lateinit var tests:CheckBox
    private lateinit var clinics:CheckBox
    private lateinit var appointmentID:String
    private  var serviceType=StringBuilder()
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var userName: TextView
    private lateinit var navHeader : View
    private lateinit var progress:ProgressBar
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appoitnment)
        supportActionBar!!.elevation= 0F
        init()
        //navigation bar
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
            progress.visibility=View.VISIBLE
            sendRequest()
        }
    }
    private fun sendRequest() {
        if (rays.isChecked){ serviceType.append(rays.text.toString()).append(", ")}
        if (clinics.isChecked){serviceType.append(clinics.text.toString()).append(", ")}
        if (tests.isChecked){serviceType.append(tests.text.toString()).append(", ")}
        appointmentID=database2.push().key!!
        val appointment= AppointmentData(appointmentID,null,null,pref.prefID.toString(), serviceType.toString(),null,fullName.text.toString(), phoneNumber = phone.text.toString() )
        database2.child(appointmentID).setValue(appointment).addOnCompleteListener {
            if (it.isSuccessful){
                progress.visibility=View.GONE
                Toast.makeText(applicationContext,R.string.successRequest,Toast.LENGTH_LONG).show()
                home()
            }else{
                Toast.makeText(applicationContext,R.string.failRequest,Toast.LENGTH_LONG).show()
            }
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
        bookBtn=findViewById(R.id.sendRequestBtn)
        rays=findViewById(R.id.medical_Rays)
        tests=findViewById(R.id.medical_tests)
        context=this
        pref= Prefrences(context)
        clinics=findViewById(R.id.medical_clinics)
        phone =findViewById(R.id.phone_edt)
        fullName=findViewById(R.id.fullName_edt)
        progress=findViewById(R.id.progress_bar)
        fullName.setText(pref.userName)
        userName=navHeader.findViewById(R.id.user_name)
        userName.setText(pref.userName)
        database.child("Patients").child(pref.prefID.toString()).get().addOnSuccessListener {
            if (it.exists()){
                phone.setText(it.child("phone").value.toString())
            }
        }
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

}