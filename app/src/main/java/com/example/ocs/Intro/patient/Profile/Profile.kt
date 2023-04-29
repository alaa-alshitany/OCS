package com.example.ocs.Intro.patient.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.ocs.Intro.Login.PatientData
import com.example.ocs.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class Profile : AppCompatActivity() {
    private lateinit var intent2: Intent
    private lateinit var patientID:String
    private  var dataBase: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var name:TextView
    private lateinit var birthDate:TextView
    private lateinit var phone:TextView
    private lateinit var address:TextView
    private lateinit var email:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_patient)

        init()
        getExtras()
        getDataDB()
    }
    private fun init(){
        intent2=intent
        name=findViewById(R.id.patientName)
        birthDate=findViewById(R.id.birthdate_txt)
        phone=findViewById(R.id.phone_txt)
        email=findViewById(R.id.email_txt)
        address=findViewById(R.id.address_txt)
    }
    private fun getDataDB(){
        dataBase.child("Patients").child(patientID).get().addOnSuccessListener {
            if (it.exists()){
                name.text = it.child("firstName").value.toString().plus(" ").plus(it.child("lastName").value.toString())
                address.text = it.child("address").value.toString()
                birthDate.text = it.child("birthDate").value.toString()
                phone.text = it.child("phone").value.toString()
                email.text = it.child("email").value.toString()
            }
                    }
    }
    private fun getExtras(){
        patientID=intent2.getStringExtra("patientID").toString()
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
}