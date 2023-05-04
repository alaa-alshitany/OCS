package com.example.ocs.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ocs.R
import com.example.ocs.databinding.ActivityDashboardBinding
import com.example.ocs.patient.services.OnCardItemClickListener

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
            val cardprofileIntent = Intent(this, profile::class.java)
            startActivity(cardprofileIntent)
        }
        binding.appointments.setOnClickListener {
            val cardappointmentIntent = Intent(this, recycle_request::class.java)
            startActivity(cardappointmentIntent)
        }
        binding.doctors.setOnClickListener {
            val carddoctorsIntent = Intent(this, Doctors::class.java)
            startActivity(carddoctorsIntent)
        }
    }


}