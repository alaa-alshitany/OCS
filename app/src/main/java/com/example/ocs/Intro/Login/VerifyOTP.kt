package com.example.ocs.Intro.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ocs.R
import com.google.firebase.auth.FirebaseAuth

class VerifyOTP : AppCompatActivity() {
    private lateinit var digit1:EditText
    private lateinit var digit2:EditText
    private lateinit var digit3:EditText
    private lateinit var digit4:EditText
    private lateinit var digit5:EditText
    private lateinit var digit6:EditText
    private lateinit var codeEntered:String
    private lateinit var continueTxt:TextView
    private lateinit var codeSent:String
    private lateinit var intent2: Intent
    private lateinit var phoneNumber:String
    private lateinit var phoneTxt:TextView
    private var firebaseAuth:FirebaseAuth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)
        bindingItems()
        getIntentExtra()
        continueTxt.setOnClickListener {
            checkCode(codeSent,codeEntered)
        }
    }
    private fun checkCode(codeSent: String,codeEntered: String) {
        if (codeSent.equals(codeEntered)){
            resetPassword(phoneNumber)
        }
        else{
            Toast.makeText(applicationContext,R.string.codeWrong,Toast.LENGTH_LONG).show()
        }
    }
    private fun resetPassword(phone: String){
        startActivity(Intent(applicationContext,ResetPassword::class.java).putExtra("phone",phone))
        finish()
    }
    private fun getIntentExtra(){
        phoneNumber=intent2.getStringExtra("phone").toString()
        phoneTxt.setText(phoneNumber)
    }
    private fun bindingItems(){
        digit1=findViewById(R.id.code1)
        digit2=findViewById(R.id.code2)
        digit3=findViewById(R.id.code3)
        digit4=findViewById(R.id.code4)
        digit5=findViewById(R.id.code5)
        digit6=findViewById(R.id.code6)
        continueTxt=findViewById(R.id.continue_btn)
        codeEntered=digit1.text.toString().plus(digit2.text.toString()).plus(digit3.text.toString()).plus(digit4.text.toString()).plus(digit5.text.toString()).plus(digit6.text.toString())
        intent2=intent
        phoneTxt=findViewById(R.id.phone_txt)

    }
}