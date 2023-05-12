package com.example.ocs.Admin.Dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ocs.Admin.Appointments.Appointments
import com.example.ocs.Admin.Doctors.Doctors
//import com.example.ocs.Admin.profile
import com.example.ocs.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity()  {
    //listener on dashboard_card
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.elevation= 0F
        //listener on dashboard_card
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profile.setOnClickListener {
            //val cardProfileIntent = Intent(this, profile::class.java)
            //startActivity(cardProfileIntent)
        }
        binding.appointments.setOnClickListener {
            val cardAppointmentIntent = Intent(this, Appointments::class.java)
            startActivity(cardAppointmentIntent)
        }
        binding.doctors.setOnClickListener {
            val cardDoctorsIntent = Intent(this, Doctors::class.java)
            startActivity(cardDoctorsIntent)
        }
        binding.logout.setOnClickListener {

        }
    }


}