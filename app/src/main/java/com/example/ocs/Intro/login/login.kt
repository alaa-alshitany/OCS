package com.example.ocs.Intro.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.ocs.R

class login : AppCompatActivity() {
    lateinit var register_btn:Button
    lateinit var activity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        register_btn=findViewById(R.id.register_btn)
        register_btn.setOnClickListener { moveToRegister()}
    }

    private fun moveToRegister() {
        activity=this
        startActivity(Intent(activity,register::class.java))
       finish()
    }
}