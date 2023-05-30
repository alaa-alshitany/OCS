package com.example.ocs.Doctor.Model

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ocs.R
import com.example.ocs.databinding.ActivityPreModelBinding

class Pre_model : AppCompatActivity() {
    private lateinit var binding: ActivityPreModelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_model)
        binding = ActivityPreModelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.elevation= 0F
        binding.predict.setOnClickListener {
            startActivity(Intent(this,Predict::class.java))
        }
        binding.upload.setOnClickListener {
            startActivity(Intent(this,UploadFiles::class.java))
        }
    }
}