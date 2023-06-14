package com.example.ocs.Login_Register.forgetPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ocs.Patient.PatientData
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
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        supportActionBar!!.elevation= 0F
        init()
        getIntentExtra()
        updateBtn.setOnClickListener {
            progress.visibility= View.VISIBLE
            if(checkPassword()){
                updateDB()
                login()
            }
        }
    }
    private fun login() {
        progress.visibility=View.GONE
        startActivity(Intent(this, com.example.ocs.Login_Register.login.Login::class.java).putExtra("hint",R.string.p_email_hint.toString()))
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

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,R.string.updateFailed,Toast.LENGTH_LONG).show()
            }

        })
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
        progress=findViewById(R.id.progress_bar)
    }
}