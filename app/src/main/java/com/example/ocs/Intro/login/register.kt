package com.example.ocs.Intro.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.ocs.R


class register : AppCompatActivity() {
    lateinit var continue_btn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        bindingItems()
        continue_btn.setOnClickListener { moveToContinueRegister() }
    }

    private fun moveToContinueRegister() {
        startActivity(Intent(this,register_continue::class.java))
    }

    private fun bindingItems(){
       continue_btn=findViewById(R.id.continue_register_btn)
    }
}