package com.example.ocs.Admin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ocs.Admin.Appointments.Appointments
import com.example.ocs.Patient.Profile.Profile
import com.example.ocs.R
import com.example.ocs.Admin.Dashboard.Dashboard
import com.example.ocs.Admin.Doctors.DoctorData
import com.example.ocs.Admin.Doctors.Doctors
import com.example.ocs.Login_Register.login.Login
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class DoctorDetails: AppCompatActivity() {
    private lateinit var fullName:EditText
    private lateinit var id:EditText
    private lateinit var email:EditText
    private lateinit var phone:EditText
    private lateinit var specialization:EditText
    private lateinit var gender:EditText
    private lateinit var password:EditText
    private lateinit var birthDate:EditText
    private lateinit var updateBtn:FloatingActionButton
    private lateinit var toggle: ActionBarDrawerToggle
    private  var database:DatabaseReference=FirebaseDatabase.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.doctor_details)
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

        updateBtn.setOnClickListener {
           var ref= database.child("Doctors").child(id.text.toString())
           ref.child("birthDate").setValue( birthDate.text.toString()).addOnSuccessListener {
               ref.child("phone").setValue( phone.text.toString()).addOnSuccessListener {
                   ref.child("email").setValue(email.text.toString()).addOnSuccessListener {
                       ref.child("password").setValue(password.text.toString()).addOnSuccessListener {
                           Toast.makeText(baseContext, "All Data has been updated!", Toast.LENGTH_LONG).show()
                           doctor()
                       }
                           }
                       }
                   }
        }
    }
//nav_bar

    private fun dashboard() {
        startActivity(Intent(this, Dashboard::class.java))
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

    private fun requests() {
        startActivity(Intent(this, Appointments::class.java))
    }

    private fun adminProfile() {
        startActivity(Intent(this, Profile::class.java))
    }

    private fun doctor() {
        startActivity(Intent(this, Doctors::class.java))
    }
    private fun init(){
        var intent2:Intent=intent

        fullName=findViewById(R.id.doctorDetailsNameTxt)
        fullName.setText(intent2.getStringExtra("name").toString())

       phone=findViewById(R.id.doctorDetailsPhoneTxt)
       phone.setText(intent2.getStringExtra("phone").toString())

        email=findViewById(R.id.doctorDetailsEmailTxt)
        email.setText(intent2.getStringExtra("email").toString())

        password=findViewById(R.id.doctorDetailsPasswordTxt)
        password.setText(intent2.getStringExtra("password").toString())

        id=findViewById(R.id.doctorDetailsIDTxt)
        id.setText(intent2.getStringExtra("id").toString())

        gender=findViewById(R.id.doctorDetailsGenderTxt)
        gender.setText(intent2.getStringExtra("gender").toString())

        birthDate=findViewById(R.id.doctorDetailsBirthTxt)
        birthDate.setText(intent2.getStringExtra("birthdate").toString())

        specialization=findViewById(R.id.doctorDetailsSpecializationTxt)
        specialization.setText(intent2.getStringExtra("specialization").toString())

        updateBtn=findViewById(R.id.updateBtn)

    }
    //nav_bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}