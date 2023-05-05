package com.example.ocs.Login_Register.register

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ocs.patient.PatientData
import com.example.ocs.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.Period
import java.util.*

class RegisterContinue : AppCompatActivity() {
    private lateinit var registerBtn: Button
    private lateinit var intent2: Intent
    private lateinit var birthDateEdt: EditText
    private lateinit var addressEdt: EditText
    private lateinit var passwordEdt: EditText
    private lateinit var passwordConfEdt: EditText
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Patients")
    private lateinit var auth: FirebaseAuth
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var phone: String
    private lateinit var email: String
    private lateinit var gender: String
    private lateinit var currentDate: LocalDate
    private lateinit var dateEntered:LocalDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continue_register)
        supportActionBar!!.elevation= 0F
        backArrow()
        init()
        getIntentExtra()
        registerBtn.setOnClickListener {
            if (checkInternet(this)) {
                if (validateInputData()) {
                    register()
                }
            } else {
                Toast.makeText(applicationContext, R.string.internetError, Toast.LENGTH_LONG).show()
            }
        }
        birthDateEdt.setOnClickListener { calenderShow() }
    }
    private fun validateInputData(): Boolean {
        if (passwordEdt.text.toString().isNotEmpty() && passwordConfEdt.text.toString()
                .isNotEmpty() && passwordEdt.text.toString().trim().length >= 8
            && birthDateEdt.text.toString().isNotEmpty() && addressEdt.text.toString()
                .isNotEmpty() && passwordEdt.text.toString().equals(passwordConfEdt.text.toString()) && Period.between(dateEntered,currentDate).years >15
        ) {
            return true
        } else {
            if (passwordEdt.text.toString().isEmpty() && passwordConfEdt.text.toString().isEmpty() &&
                birthDateEdt.text.toString().isEmpty() && addressEdt.text.toString().isEmpty()) {
                passwordEdt.setError(getText(R.string.requird))
                passwordConfEdt.setError(getText(R.string.requird))
                birthDateEdt.setError(getText(R.string.requird))
                addressEdt.setError(getText(R.string.requird))
                Toast.makeText(applicationContext, R.string.emptyAllData, Toast.LENGTH_LONG).show()
            }

            else if (birthDateEdt.text.toString().isEmpty()) {
                birthDateEdt.setError(getText(R.string.requird))
                Toast.makeText(applicationContext,R.string.emptyBirthDate,Toast.LENGTH_LONG).show()
            }else if (Period.between(dateEntered,currentDate).years <15){
                birthDateEdt.setError(getText(R.string.ageError))
                Toast.makeText(applicationContext,R.string.ageError,Toast.LENGTH_LONG).show()
            }
            else if (addressEdt.text.toString().isEmpty()) {
                addressEdt.setError(getText(R.string.requird))
                Toast.makeText(applicationContext,R.string.emptyAddress,Toast.LENGTH_LONG).show()
            }

            else if (passwordEdt.text.toString().isEmpty() || passwordConfEdt.text.toString().isEmpty()) {
                passwordConfEdt.setError(getText(R.string.requird))
                passwordEdt.setError(getText(R.string.requird))
                Toast.makeText(applicationContext, R.string.emptyPassword, Toast.LENGTH_LONG).show()
            } else if (passwordEdt.text.toString().trim().length < 8) {
                passwordEdt.setError(getText(R.string.notValidPassword))
                Toast.makeText(applicationContext, R.string.notValidPassword, Toast.LENGTH_LONG).show()
            } else if (!passwordEdt.text.toString().equals(passwordConfEdt.text.toString())) {
                passwordConfEdt.setError(getText(R.string.passwordNotMatch))
                passwordEdt.setError(getText(R.string.passwordNotMatch))
                Toast.makeText(applicationContext, R.string.passwordNotMatch, Toast.LENGTH_LONG).show()
            }
            return false
        }
    }
    private fun calenderShow() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this,R.style.datepicker,
            DatePickerDialog.OnDateSetListener
            { view, year, monthOfYear, dayOfMonth ->
                birthDateEdt.setText("$year" +"-"+ (monthOfYear + 1)+"-"+dayOfMonth  )
                dateEntered=LocalDate.of(year,month,dayOfMonth)
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun register() {
        auth.createUserWithEmailAndPassword(email, passwordEdt.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val patientId = auth.currentUser!!.uid
                    val patient = PatientData(
                        patientId,
                        firstName,
                        lastName,
                        addressEdt.text.toString(),
                        gender,
                        birthDateEdt.text.toString(),
                        email,
                        null,
                        null,
                        null,
                        phone,
                        passwordEdt.text.toString()
                    )
                    database.child(patientId).setValue(patient).addOnSuccessListener {
                        Toast.makeText(this, R.string.register_success, Toast.LENGTH_LONG).show()
                        login()
                    }.addOnFailureListener { err ->
                        Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }
    private fun getIntentExtra() {
        firstName = intent2.getStringExtra("first name").toString()
        lastName = intent2.getStringExtra("last name").toString()
        phone = intent2.getStringExtra("phone").toString()
        email = intent2.getStringExtra("email").toString()
        gender = intent2.getStringExtra("gender").toString()
    }
    private fun backArrow() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
    }
    private fun login() {
        startActivity(
            Intent(this, com.example.ocs.Login_Register.login.Login::class.java).putExtra("hint", R.string.p_email_hint.toString())
        )
        Toast.makeText(this, R.string.register_success, Toast.LENGTH_LONG).show()
        finish()
    }
    private fun checkInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false
            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
    private fun init() {
        registerBtn = findViewById(R.id.register_btn)
        intent2 = intent
        birthDateEdt = findViewById(R.id.birthdate)
        addressEdt = findViewById(R.id.address)
        passwordEdt = findViewById(R.id.password)
        passwordConfEdt = findViewById(R.id.passwordConfirmation)
        auth = FirebaseAuth.getInstance()
        currentDate= LocalDate.now()
    }
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
}