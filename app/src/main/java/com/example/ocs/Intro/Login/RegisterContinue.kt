package com.example.ocs.Intro.Login

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ocs.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*

class RegisterContinue : AppCompatActivity() {
    private lateinit var registerBtn: Button
    private lateinit var intent2:Intent
    private lateinit var birthDateEdt:EditText
    private lateinit var addressEdt:EditText
    private lateinit var passwordEdt:EditText
    private lateinit var passwordConfEdt:EditText
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Patients")
    private lateinit var auth:FirebaseAuth
    private lateinit var firstName:String
    private lateinit var lastName:String
    private lateinit var phone:String
    private lateinit var email:String
    private lateinit var gender:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continue_register)
        backArrow()
        init()
        register()

    }
private fun validateInputData():Boolean{
    if (passwordEdt.text.toString().isNotEmpty() && passwordConfEdt.text.toString().isNotEmpty() && passwordEdt.text.toString().trim().length >= 8
        && birthDateEdt.text.toString().isNotEmpty() && addressEdt.text.toString().isNotEmpty() && passwordEdt.text.toString().equals(passwordConfEdt.text.toString()))
    {
        return true
    }

    else{
       if( passwordEdt.text.toString().isEmpty() && passwordConfEdt.text.toString().isEmpty() && birthDateEdt.text.toString().isEmpty() && addressEdt.text.toString().isEmpty())
       {Toast.makeText(applicationContext,R.string.emptyData,Toast.LENGTH_LONG).show()}

        if (birthDateEdt.text.toString().isEmpty())
        {Toast.makeText(applicationContext,R.string.emptyBirthDate,Toast.LENGTH_LONG).show()}

        if (addressEdt.text.toString().isEmpty()) {Toast.makeText(applicationContext,R.string.emptyAddress,Toast.LENGTH_LONG).show()}

        if (passwordEdt.text.toString().isEmpty() || passwordConfEdt.text.toString().isEmpty()) {
            Toast.makeText(applicationContext,R.string.emptyPassword,Toast.LENGTH_LONG).show()
        } else if (passwordEdt.text.toString().trim().length < 8) {
            Toast.makeText(applicationContext,R.string.notValidPassword,Toast.LENGTH_LONG).show()
        } else if (!passwordEdt.text.toString().equals(passwordConfEdt.text.toString())){
            Toast.makeText(applicationContext,R.string.passwordNotMatch,Toast.LENGTH_LONG).show()}

        return false
    }

}
    private fun register() {
        getIntentExtra()
        registerBtn.setOnClickListener {
            if (validateInputData()){
                sendDataToDB()
                moveToLogin()
            }
        }
        }
    private fun calenderShow() {
        val calendar= Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener
            { view, year, monthOfYear, dayOfMonth ->
                birthDateEdt.setText("" + dayOfMonth + " - " + (monthOfYear+1) + " - " + year)
            }, year, month, day)
        datePickerDialog.show()
    }
    private fun sendDataToDB() {
        auth.createUserWithEmailAndPassword(email,passwordEdt.text.toString()).addOnCompleteListener {
            if (it.isSuccessful){
                val patientId=auth.currentUser!!.uid
                val patient= PatientData(patientId,firstName,lastName, addressEdt.text.toString(),gender,birthDateEdt.text.toString(),email,null,null,null,phone,passwordEdt.text.toString())
                database.child(patientId).setValue(patient).addOnSuccessListener{
                    Toast.makeText(this,R.string.register_success,Toast.LENGTH_LONG).show()
                }.addOnFailureListener { err->
                    Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_LONG).show() }
            }
            }
        }
    private fun getIntentExtra(){
         firstName=intent2.getStringExtra("first name").toString()
         lastName=intent2.getStringExtra("last name").toString()
         phone=intent2.getStringExtra("phone").toString()
         email=intent2.getStringExtra("email").toString()
         gender=intent2.getStringExtra("gender").toString()
    }
    private fun backArrow(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
    }
    private fun moveToLogin() {
       startActivity(Intent(this,login::class.java).putExtra("hint",R.string.p_email_hint.toString()))
        Toast.makeText(this,R.string.register_success,Toast.LENGTH_LONG).show()
        finish()
    }

    private fun init(){
        registerBtn=findViewById(R.id.register_btn)
        intent2=intent
        birthDateEdt=findViewById(R.id.birthdate)
        birthDateEdt.setOnClickListener { calenderShow() }
        addressEdt=findViewById(R.id.address)
        passwordEdt=findViewById(R.id.password)
        passwordConfEdt=findViewById(R.id.passwordConfirmation)
        auth=FirebaseAuth.getInstance()
    }
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
}