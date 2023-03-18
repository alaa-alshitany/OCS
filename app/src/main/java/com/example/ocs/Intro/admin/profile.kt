package com.example.ocs.Intro.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ocs.R

class profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
    }
}