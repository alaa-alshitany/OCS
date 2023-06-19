package com.example.ocs.Login_Register.forgetPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.chaos.view.PinView
import com.example.ocs.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerifyOTP : AppCompatActivity() {
    private lateinit var codeEntered:String
    private lateinit var continueTxt:TextView
    private lateinit var codeSent:String
    private lateinit var intent2: Intent
    private lateinit var phoneNumber:String
    private lateinit var phoneTxt:TextView
    private lateinit var pinView: PinView
   private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)
        supportActionBar!!.elevation= 0F
        init()
        getIntentExtra()
        continueTxt.setOnClickListener {
            codeEntered=pinView.text.toString().trim()
            checkCode(codeSent,codeEntered)
        }
    }
    private fun checkCode(codeSent: String,codeEntered: String) {
        //credential=PhoneAuthProvider.getCredential(codeSent,codeEntered)
        Log.d("code sent",codeSent)
        Log.d("code entered",codeEntered)
        if (codeEntered.length!=6){
            Toast.makeText(applicationContext,R.string.wrongOTP,Toast.LENGTH_LONG).show()
        }
       else {
           val credential =PhoneAuthProvider.getCredential(codeSent,codeEntered)
           signInWithPhoneAuthCredential(credential)

        }

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(object: OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext,R.string.sucessVerification,Toast.LENGTH_LONG).show()
                        resetPassword(phoneNumber)
                    } else {
                        Toast.makeText(applicationContext, task.exception!!.message, Toast.LENGTH_LONG).show()
                    }
              }
         })
    }
    override fun onStart() {
        super.onStart()
        init()
        getIntentExtra()
    }
    private fun resetPassword(phone: String){
        startActivity(Intent(applicationContext, ResetPassword::class.java).putExtra("phone",phone))
        finish()
    }
    private fun getIntentExtra(){
        phoneNumber=intent2.getStringExtra("phone").toString()
        codeSent=intent2.getStringExtra("codeSent").toString()
        phoneTxt.setText(phoneNumber)


    }
    private fun init(){
        pinView=findViewById(R.id.pinView)
        intent2=intent
        continueTxt=findViewById(R.id.continue_btn)
        phoneTxt=findViewById(R.id.phone_txt)
        auth= FirebaseAuth.getInstance()
        codeSent=""
    }
}