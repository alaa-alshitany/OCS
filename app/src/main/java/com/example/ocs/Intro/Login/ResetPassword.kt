package com.example.ocs.Intro.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ocs.R

class ResetPassword : AppCompatActivity() {
    private lateinit var password:EditText
    private lateinit var passwordConfirmation:EditText
    private lateinit var updateBtn:Button
    private lateinit var intent2: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        bindingItems()
        getIntentExtra()
        updateBtn.setOnClickListener {
            if(checkPassword()){

            }
        }
    }

    private fun getIntentExtra() {

    }

    private fun checkPassword():Boolean {
        if (password.text.toString().isNotEmpty() && passwordConfirmation.text.toString()
                .isNotEmpty() && password.text.toString()
                .trim().length >= 8 && password.text.toString()
                .equals(passwordConfirmation.text.toString())
        ) {
            return true
        } else {
            if (password.text.toString().isEmpty() || passwordConfirmation.text.toString().isEmpty())
            { Toast.makeText(applicationContext, R.string.emptyPassword, Toast.LENGTH_LONG).show()}
            else if (!password.text.toString().equals(passwordConfirmation.text.toString()))
            {Toast.makeText(applicationContext,R.string.passwordNotMatch,Toast.LENGTH_LONG).show()}
                return false
            }
        }

    private fun bindingItems(){
        password=findViewById(R.id.password_edt)
        passwordConfirmation=findViewById(R.id.passwordConfirmation)
        updateBtn=findViewById(R.id.update_btn)
        intent2=intent
    }
}