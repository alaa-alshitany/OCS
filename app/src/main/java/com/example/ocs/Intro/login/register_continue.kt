package com.example.ocs.Intro.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import com.example.ocs.R

class register_continue : AppCompatActivity() {
    lateinit var register_btn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continue_register)
        bindingItems()
        register_btn.setOnClickListener { moveToLogin() }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
    }

    private fun moveToLogin() {
       startActivity(Intent(this,login::class.java).putExtra("hint",R.string.p_email_hint.toString()))
        finish()
    }

    private fun bindingItems(){
        register_btn=findViewById(R.id.register_btn)
    }
}