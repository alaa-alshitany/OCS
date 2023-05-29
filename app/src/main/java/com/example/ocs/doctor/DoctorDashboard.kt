package com.example.ocs.doctor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.R
import com.example.ocs.databinding.ActivityDoctorDashboardBinding

class DoctorDashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorDashboardBinding
    private lateinit var pref: Prefrences
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_doctor_dashboard)

        //listener on dashboard_card
        binding = ActivityDoctorDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.elevation= 0F
        init()

        binding.patients.setOnClickListener {
            val cardpatientIntent = Intent(this, Patient_recycle::class.java)
            startActivity(cardpatientIntent)
        }
        binding.appointmentsD.setOnClickListener {
            val cardAppointmentIntent = Intent(this, Recycle_book::class.java)
            startActivity(cardAppointmentIntent)
        }
        binding.model.setOnClickListener {
            val cardDoctorsIntent = Intent(this, model::class.java)
            startActivity(cardDoctorsIntent)
        }
        binding.logoutD.setOnClickListener {
            pref.prefClear()
            moveToLogin()
        }
    }


    private fun init() {
        context = this
        pref = Prefrences(context)
    }
    private fun moveToLogin() {
        startActivity(Intent(this, Login::class.java).putExtra("hint", R.string.a_email_hint.toString()))
        Toast.makeText(this, R.string.logout, Toast.LENGTH_LONG).show()
        finish()
    }

}