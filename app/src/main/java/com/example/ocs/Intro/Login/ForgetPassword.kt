package com.example.ocs.Intro.Login

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.ocs.R
import com.google.firebase.database.*
import kotlin.random.Random

class ForgetPassword : AppCompatActivity() {
   private lateinit var phoneEdt:EditText
   private lateinit var sendSmsButton: Button
   private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val smsManager: SmsManager =  SmsManager.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        backArrow()
        bindingItems()
        sendSmsButton.setOnClickListener {
        checkUser(phoneEdt.text.toString()) }

    }
private fun checkPermission(){
    if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this, arrayOf( android.Manifest.permission.SEND_SMS),111)
    }

}
private fun sendSMS(phone:String){
    var code=Random.nextInt(100000,999999)
    smsManager.sendTextMessage(phone, null, "Your Verification Code is : $code", null, null)
    Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()

}
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==111 &&grantResults[0] ==PackageManager.PERMISSION_GRANTED){

        }
    }
    private fun bindingItems(){
        phoneEdt=findViewById(R.id.phone)
        sendSmsButton=findViewById(R.id.send_btn)
    }
    private fun checkUser(phone:String){
        val queryPhone:Query=database.child("Patients").orderByChild("phone").equalTo(phone)
        queryPhone.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    checkPermission()
                    sendSMS(phone)
                    startActivity(Intent(applicationContext,VerifyOTP::class.java).putExtra("phone",phone))
                    finish()
                }else{
                    Toast.makeText(applicationContext,R.string.userNotFound,Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun backArrow(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
    }
}