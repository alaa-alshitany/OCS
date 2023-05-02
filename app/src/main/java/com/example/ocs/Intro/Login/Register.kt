package com.example.ocs.Intro.Login
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ocs.R
import com.google.firebase.database.*

class Register : AppCompatActivity() {
    lateinit var continueBtn: TextView
    lateinit var firstNameEdt:EditText
    lateinit var lastNameEdt:EditText
    lateinit var emailEdt:EditText
    lateinit var phoneEdt:EditText
    lateinit var genderRadio:RadioGroup
    lateinit var radioButton: RadioButton
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
        continueBtn.setOnClickListener {
            if(checkInternet(this)){
                checkUser(phoneEdt.text.toString())
            }else{
                Toast.makeText(applicationContext,R.string.internetError,Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
    private fun continueRegister() {
        var selectedGender:Int=genderRadio!!.checkedRadioButtonId
        radioButton = findViewById(selectedGender)
        if (validateInputData()){
            val intent = Intent(this,RegisterContinue::class.java)
            intent.putExtra("first name",firstNameEdt.text.toString())
            intent.putExtra("last name",lastNameEdt.text.toString())
            intent.putExtra("email", emailEdt.text.toString())
            intent.putExtra("phone", phoneEdt.text.toString())
            intent.putExtra("gender",radioButton.text.toString())
            startActivity(intent)
        }
    }
    private fun init(){
        continueBtn=findViewById(R.id.continue_btn)
        firstNameEdt=findViewById(R.id.firstName)
        lastNameEdt=findViewById(R.id.lastName)
        emailEdt=findViewById(R.id.email)
        phoneEdt=findViewById(R.id.phone)
        genderRadio=findViewById(R.id.gender)
    }
    private fun validateInputData() :Boolean{
        var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        var phonePattern="^01[0125][0-9]{8}\$"
         if(firstNameEdt.text.toString().isNotEmpty() && lastNameEdt.text.toString().isNotEmpty()
             && emailEdt.text.toString().trim().matches(emailPattern.toRegex())
             && emailEdt.text.toString().isNotEmpty() && phoneEdt.text.toString().isNotEmpty()
             && phoneEdt.text.toString().length ==11 && phoneEdt.text.toString().trim().matches(phonePattern.toRegex())){
             return true
         }else{
             if(emailEdt.text.toString().trim().isEmpty() && firstNameEdt.text.toString().isEmpty()
                 &&(lastNameEdt.text.toString().isEmpty() && phoneEdt.text.toString().isEmpty())){
                 emailEdt.setError(getText(R.string.requird))
                 firstNameEdt.setError(getText(R.string.requird))
                 lastNameEdt.setError(getText(R.string.requird))
                 phoneEdt.setError(getText(R.string.requird))
                 Toast.makeText(this,R.string.emptyAllData,Toast.LENGTH_LONG).show()
             }

             if (emailEdt.text.toString().trim().isEmpty()){
                 emailEdt.setError(getText(R.string.requird))
                 Toast.makeText(this, R.string.emptyEmail, Toast.LENGTH_LONG).show()
             } else if (!emailEdt.text.toString().trim().matches(emailPattern.toRegex())){
                 emailEdt.setError(getText(R.string.notValidEmail))
                 Toast.makeText(this, R.string.notValidEmail, Toast.LENGTH_LONG).show()
             }
             if (firstNameEdt.text.toString().isEmpty()){
                 firstNameEdt.setError(getText(R.string.requird))
                 Toast.makeText(this, R.string.emptyFName, Toast.LENGTH_LONG).show()
             }
             if (lastNameEdt.text.toString().isEmpty()){
                 lastNameEdt.setError(getText(R.string.requird))
                 Toast.makeText(this, R.string.emptyLName, Toast.LENGTH_LONG).show()
             }
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
    private fun checkUser(phone:String){
        val queryPhone: Query =database.child("Patients").orderByChild("phone").equalTo(phone)
        queryPhone.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    Toast.makeText(applicationContext,R.string.userFound,Toast.LENGTH_LONG).show()
                }else{
                    continueRegister()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun checkInternet(context:Context) : Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
    private fun calculateAge(){

    }
    }