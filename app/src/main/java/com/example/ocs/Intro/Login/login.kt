package com.example.ocs.Intro.Login
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.ocs.Intro.patient.services.services
import com.example.ocs.Intro.admin.profile
import com.example.ocs.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class login : AppCompatActivity() {
    private lateinit var registerBtn:Button
    private lateinit var activity: Activity
    private lateinit var loginBtn:Button
    private lateinit var email_edt:EditText
    private lateinit var loginTextview:TextView
    private lateinit var newuserTextview:TextView
    private lateinit var loginImg:ImageView
    private lateinit var emailLayout:TextInputLayout
    private lateinit var forgetPasswordBtn:Button
    private lateinit var passwordEdt:EditText
    private lateinit var context: Context
    private lateinit var pref:Prefrences
    private lateinit var intent2:Intent
    private lateinit var patientID:String
    private val database:DatabaseReference=FirebaseDatabase.getInstance().reference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        getIntentExtra()
        registerBtn.setOnClickListener { register()}
        forgetPasswordBtn.setOnClickListener { forgetPassword() }

    }
    private fun checkInternet(context: Context) : Boolean{
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
    private fun forgetPassword() {
        startActivity(Intent(activity,ForgetPassword::class.java))
    }
    private fun getIntentExtra(){
        var hint:String =intent.getStringExtra("email_hint").toString()
        when(hint){
            R.string.d_email_hint.toString() -> doctorLogin()
            R.string.a_email_hint.toString() -> adminLogin()
            R.string.p_email_hint.toString() -> patientLogin()
            else -> {
                patientLogin()
            }
        }
    }
    private fun home() {
        startActivity(Intent(activity, services::class.java).putExtra("patientID",pref.prefID))
        finish()
    }
    private fun register() {
        startActivity(Intent(activity,Register::class.java))
    }
    private fun doctorLogin(){
        email_edt.setHint(R.string.d_email_hint)
        loginTextview.setText(R.string.d_login)
        registerBtn.visibility = View.INVISIBLE
        newuserTextview.visibility = View.INVISIBLE
    }
    private fun adminLogin(){
        email_edt.setHint(R.string.a_email_hint)
        loginTextview.setText(R.string.a_login)
        registerBtn.visibility = View.INVISIBLE
        newuserTextview.visibility = View.INVISIBLE
        loginImg.setImageResource(R.drawable.baseline_admin_panel_settings_24)
        forgetPasswordBtn.visibility=View.INVISIBLE
        emailLayout.isPasswordVisibilityToggleEnabled=true
        loginBtn.setOnClickListener { startActivity(Intent(this,profile::class.java)) }

    }
    private fun patientLogin(){
        email_edt.setHint(R.string.p_email_hint)
        loginBtn.setOnClickListener {
            if(checkInternet(this)){
            checkEmptyData(email_edt,passwordEdt)
            readPatientData(email_edt.text.toString(),passwordEdt.text.toString())
        }else{
                Toast.makeText(applicationContext,R.string.internetError,Toast.LENGTH_LONG).show()
        }}
    }
    private fun readPatientData(email: String,password: String) {
        var queryPatient: Query = database.child("Patients").orderByChild("email").equalTo(email)
        queryPatient.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        var patient = item.getValue<PatientData>()
                        if (patient != null) {
                            if (patient.password.equals(password)) {
                               patientID= patient?.id.toString()
                                pref.prefStatus = true
                                pref.prefLevel="patient"
                                pref.prefID=patientID
                                pref.userName=patient?.firstName.toString().plus(" ${patient?.lastName.toString()}")
                                Toast.makeText(context, R.string.login_success, Toast.LENGTH_LONG).show()
                                home()
                            }else{
                                passwordEdt.setError(getText(R.string.login_failed))
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun onStart(){
        super.onStart()
        if (pref.prefStatus){
            when(pref.prefLevel){
                "patient" -> home()
                //"doctor" ->
            }

        }
    }
    private fun checkEmptyData(email:EditText, password:EditText){
        if (email.text.toString().isEmpty() && password.text.toString().isEmpty()){
            email.setError(getText(R.string.requird))
            password.setError(getText(R.string.requird))
            Toast.makeText(applicationContext,R.string.emptyAllData,Toast.LENGTH_LONG).show()
        }

        else if (email.text.toString().isEmpty()){
            email.setError(getText(R.string.requird))
            Toast.makeText(applicationContext,R.string.emptyEmail,Toast.LENGTH_LONG).show()
        }
        else if(password.text.toString().isEmpty()){
            password.setError(getText(R.string.requird))
            Toast.makeText(applicationContext,R.string.emptyPassword,Toast.LENGTH_LONG).show()
        }
    }
    private fun init(){
        email_edt=findViewById(R.id.email)
        loginTextview=findViewById(R.id.login_text_view)
        registerBtn=findViewById(R.id.register_btn)
        loginBtn=findViewById(R.id.login_btn)
        newuserTextview=findViewById(R.id.newUser_txtView)
        loginImg=findViewById(R.id.login_img)
        emailLayout=findViewById(R.id.email_input_layout)
        forgetPasswordBtn=findViewById(R.id.forgetPass_btn)
        passwordEdt=findViewById(R.id.password_edt)
        context=this
        pref= Prefrences(context)
        intent2=intent
        activity=this
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if (id == R.id.doctor) {
            val intent = Intent(this, this::class.java)
            intent.putExtra("email_hint",R.string.d_email_hint.toString())
            this.startActivity(intent)
            return true
        }

        if (id == R.id.admin) {
            val intent = Intent(this, this::class.java)
            intent.putExtra("email_hint",R.string.a_email_hint.toString())
            this.startActivity(intent)
            return true
        }
        if(id==R.id.patient){
            val intent = Intent(this, this::class.java)
            intent.putExtra("email_hint",R.string.p_email_hint.toString())
            this.startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}