package com.example.ocs.Intro.Login

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer.OnCompletionListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.ocs.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class ForgetPassword : AppCompatActivity() {
   private lateinit var phoneEdt:EditText
   private lateinit var sendSmsButton: Button
   private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var auth:FirebaseAuth=FirebaseAuth.getInstance()
    private lateinit var progressBar:ProgressBar
    private lateinit var verficationCode:String
    private lateinit var resendingToken : PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        backArrow()
        init()
        sendSmsButton.setOnClickListener {
        checkUser(phoneEdt.text.toString()) }
    }

@SuppressLint("SuspiciousIndentation")
    private fun sendOTP(phone:String, isResend:Boolean){
        setInProgress(true)
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(p0)
                    setInProgress(false)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(applicationContext,R.string.verifyFailed,Toast.LENGTH_LONG).show()
                    setInProgress(false)
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    verficationCode=p0
                    resendingToken=p1
                    Toast.makeText(applicationContext, R.string.codeSent, Toast.LENGTH_LONG).show()
                    setInProgress(false)
                    startActivity(Intent(applicationContext, VerifyOTP::class.java).putExtra("codeSent",verficationCode).putExtra("phone", phoneEdt.text.toString()))
                    finish()
                }
            })
            if (isResend){
                PhoneAuthProvider.verifyPhoneNumber(options.setForceResendingToken(resendingToken).build())
            }else{
                PhoneAuthProvider.verifyPhoneNumber(options.build())
            }
        }
    private fun setInProgress(inProgress: Boolean){
        if (inProgress){
            progressBar.visibility=View.VISIBLE
            sendSmsButton.visibility=View.GONE
        }else{
            progressBar.visibility=View.INVISIBLE
            sendSmsButton.visibility=View.VISIBLE
        }
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        setInProgress(true)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(object: OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult?>) {
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext,"success",Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, task.exception!!.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    private fun init(){
        phoneEdt=findViewById(R.id.phone)
        sendSmsButton=findViewById(R.id.send_btn)
        progressBar=findViewById(R.id.progressBar)
        progressBar.visibility= View.INVISIBLE
    }
    private fun checkUser(phone:String){
        if (checkEmpty()){
        val queryPhone:Query=database.child("Patients").orderByChild("phone").equalTo(phone)
        queryPhone.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    sendOTP("+20".plus(phone),false)
                }else{
                    Toast.makeText(applicationContext,R.string.userNotFound,Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    }

    private fun checkEmpty(): Boolean {
        var phonePattern="^01[0125][0-9]{8}\$"
       if( phoneEdt.text.toString().isNotEmpty())
           return true
        else {
          if (phoneEdt.text.toString().isEmpty()){
               phoneEdt.setError(getText(R.string.requird))
               Toast.makeText(this, R.string.emptyPhone, Toast.LENGTH_LONG).show()
           }
           else if (phoneEdt.text.toString().length != 11 || !phoneEdt.text.toString().trim().matches(phonePattern.toRegex())){
               phoneEdt.setError(getText(R.string.notValidNumber))
               Toast.makeText(this, R.string.notValidNumber, Toast.LENGTH_LONG).show()
           }
           return false
       }
    }

    private fun backArrow(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
    }
}