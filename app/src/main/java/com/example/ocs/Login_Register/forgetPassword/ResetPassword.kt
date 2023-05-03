package com.example.ocs.Login_Register.forgetPassword

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ocs.patient.PatientData
import com.example.ocs.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class ResetPassword : AppCompatActivity() {
    private lateinit var passwordEdt:EditText
    private lateinit var passwordConfirmationEdt:EditText
    private lateinit var updateBtn:Button
    private lateinit var intent2: Intent
    private lateinit var phoneNumber:String
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        supportActionBar!!.elevation= 0F
        init()
        getIntentExtra()
        updateBtn.setOnClickListener {
            if(checkPassword()){
                updateDB()
                login()
            }
        }
    }
    private fun login() {
        startActivity(Intent(this, com.example.ocs.Login_Register.login.login::class.java).putExtra("hint",R.string.p_email_hint.toString()))
        Toast.makeText(this,R.string.updateSuccess,Toast.LENGTH_LONG).show()
        finish()
    }
    private fun updateDB() {
        val query:Query=database.child("Patients").orderByChild("phone").equalTo(phoneNumber)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children){
                    var patient = item.getValue<PatientData>()
                    var patientID= patient?.id.toString()
                    database.child("Patients").child(patientID).child("password").setValue(passwordEdt.text.toString())

                    //var patientAuth:FirebaseUser
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,R.string.updateFailed,Toast.LENGTH_LONG).show()
            }

        })
/*
        patient?.updatePassword(passwordEdt.text.toString())?.addOnCompleteListener {
            if (it.isSuccessful){
                var map = mapOf(
                    "password" to passwordEdt.text.toString()
                )

                databaseRef.updateChildren(map)
                Toast.makeText(applicationContext,"Password Updated Successfully.",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext,"Password Update Failed.",Toast.LENGTH_LONG).show()
            }
        }*/
    }

    private fun getIntentExtra() {
        phoneNumber=intent2.getStringExtra("phone").toString()
    }

    private fun checkPassword():Boolean {
        if (passwordEdt.text.toString().isNotEmpty() && passwordConfirmationEdt.text.toString()
                .isNotEmpty() && passwordEdt.text.toString()
                .trim().length >= 8 && passwordEdt.text.toString()
                .equals(passwordConfirmationEdt.text.toString())
        ) {
            return true
        } else {
            if (passwordEdt.text.toString().isEmpty() || passwordConfirmationEdt.text.toString().isEmpty())
            { Toast.makeText(applicationContext, R.string.emptyPassword, Toast.LENGTH_LONG).show()}
            else if (!passwordEdt.text.toString().equals(passwordConfirmationEdt.text.toString()))
            {Toast.makeText(applicationContext,R.string.passwordNotMatch,Toast.LENGTH_LONG).show()}
                return false
            }
        }

    private fun init(){
        passwordEdt=findViewById(R.id.password)
        passwordConfirmationEdt=findViewById(R.id.passwordConfirmation)
        updateBtn=findViewById(R.id.update_btn)
        intent2=intent
    }
}