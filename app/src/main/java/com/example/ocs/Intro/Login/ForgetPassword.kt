package com.example.ocs.Intro.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ocs.R
import com.google.firebase.database.*

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
private fun sendSMS(phone:String){
    try{
        smsManager.sendTextMessage(phone, null, "message", null, null)
        Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()
    }catch (e:Exception){
        Toast.makeText(applicationContext,e.message.toString(),Toast.LENGTH_LONG).show()
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
                    startActivity(Intent(applicationContext,VerifyOTP::class.java).putExtra("phone",phone))
                    sendSMS(phoneEdt.text.toString())
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