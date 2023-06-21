package com.example.ocs.Doctor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ocs.Doctor.Appointments.Appointments
import com.example.ocs.Doctor.DoctorProfile.Profile
import com.example.ocs.Doctor.Model.Pre_model
//import com.example.ocs.Doctor.Appointments.Appointments
//import com.example.ocs.Doctor.DoctorProfile.
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class PatientDetails: AppCompatActivity() {

    private lateinit var Name: EditText
    private lateinit var email:EditText
    private lateinit var phone:EditText
    private lateinit var medicine:EditText
    private lateinit var address:EditText
    private lateinit var ic50:EditText
    private lateinit var birthdate:EditText
    private lateinit var gender:EditText
    private lateinit var pref: Prefrences
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var context: Context
    private lateinit var navHeader : View
    private lateinit var userName: TextView
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var updateBtn:FloatingActionButton
    //nav_bar
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var patientID:String
    private lateinit var treatmentID:String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.patient_details)
        supportActionBar!!.elevation=0F
        init()
        //nav_bar
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nau_profile1-> doctorProfile()
                R.id.nau_booking1-> appointment()
                R.id.nau_patient-> patients()
                R.id.nau_model-> model()
                R.id.nau_logout1-> logout()
            }

            true
        }
        updateBtn.setOnClickListener {
            var ref= database.child("Treatments").child(treatmentID)
            if (medicine.text.toString().isEmpty()){
                ref.child("drugName").setValue("Not added").addOnSuccessListener {
                    ref.child("IC50").setValue("Not added").addOnSuccessListener {
                        Toast.makeText(baseContext, "All Data has been updated!", Toast.LENGTH_LONG).show()
                        patients()
                    }
                }
            }else{
                ref.child("drugName").setValue( medicine.text.toString()).addOnSuccessListener {
                    ref.child("IC50").setValue(ic50.text.toString()).addOnSuccessListener {
                        Toast.makeText(baseContext, "All Data has been updated!", Toast.LENGTH_LONG).show()
                        patients()
                    }
                }
            }
            }
            }
    //nav_bar
    private fun logout() {
        pref.prefClear()
        moveToLogin()
    }
    private fun moveToLogin() {
        startActivity(Intent(this, Login::class.java).putExtra("hint",R.string.p_email_hint.toString()))
        Toast.makeText(this,R.string.logout, Toast.LENGTH_LONG).show()
        finish()
    }
    private fun appointment() {
        startActivity(Intent(this, Appointments::class.java))
    }

    private fun doctorProfile() {
        startActivity(Intent(this, Profile::class.java))
    }
    private fun patients() {
        startActivity(Intent(this, Patients::class.java))
    }

    private fun model() {
        startActivity(Intent(this, Pre_model::class.java))
    }
    //nav_bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

    private fun init(){
        var intent2:Intent=intent

        Name=findViewById(R.id.patientDetailsNameTxt)
        Name.setText(intent2.getStringExtra("name").toString())

        email=findViewById(R.id.patientDetailsEmailTxt)
        email.setText(intent2.getStringExtra("email").toString())

        phone=findViewById(R.id.patientDetailsPhoneTxt)
        phone.setText(intent2.getStringExtra("phone").toString())

        medicine=findViewById(R.id.patientDetailsdrugNameTxt)


        address=findViewById(R.id.patientDetailsAddressTxt)
        address.setText(intent2.getStringExtra("address").toString())

        ic50=findViewById(R.id.patientDetailsIC50Txt)

        gender=findViewById(R.id.patientDetailsGenderTxt)
        gender.setText(intent2.getStringExtra("gender").toString())

        birthdate=findViewById(R.id.patientDetailsBirthTxt)
        birthdate.setText(intent2.getStringExtra("birthdate").toString())
        patientID= intent2.getStringExtra("id").toString()
        database.child("Treatments").orderByChild("patientID").equalTo(patientID).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        var treatment = item.getValue<TreatmentData>()
                        if (treatment != null) {
                            ic50.setText(treatment.IC50.toString())
                            medicine.setText(treatment.drugName.toString())
                            treatmentID=treatment.treatmentID.toString()
                        }else{
                            medicine.setText("Not added")
                           ic50.setText("Not added")
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,"canceled",Toast.LENGTH_LONG)
            }
        })
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        navHeader=navView.getHeaderView(0)
        context=this
        pref= Prefrences(context)
        userName=navHeader.findViewById(R.id.user_name)
        userName.setText(pref.userName)
        updateBtn=findViewById(R.id.updateBtn)

    }
}
