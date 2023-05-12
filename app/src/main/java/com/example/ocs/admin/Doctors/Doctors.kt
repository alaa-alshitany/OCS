package com.example.ocs.admin.Doctors

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R
import com.example.ocs.admin.Dashboard
import com.example.ocs.admin.recycle_request
import com.example.ocs.Patient.Profile.Profile
import com.example.ocs.Patient.services.OnItemRecycleClickListener
import com.example.ocs.Patient.services.ServiceData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.Period
import java.util.*
import kotlin.collections.ArrayList

class Doctors : AppCompatActivity() , OnItemRecycleClickListener {
    private lateinit var doctorRecycle: RecyclerView
    private lateinit var doctorList: ArrayList<DoctorData>
    private lateinit var searchView:SearchView
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView : NavigationView
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var addDoctorBtn:FloatingActionButton
    private lateinit var dAdapter :DoctorAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var addBtn:Button
    private lateinit var cancelBtn:Button
    private lateinit var firstName:EditText
    private lateinit var lastName:EditText
    private lateinit var specialization:EditText
    private lateinit var birthDate:EditText
    private lateinit var genderRadio: RadioGroup
    private lateinit var radioButton: RadioButton
    private lateinit var email:EditText
    private lateinit var phone:EditText
    private lateinit var password:EditText
    private  var currentDate: LocalDate=LocalDate.now()
    private lateinit var dateEntered: LocalDate
   private lateinit var  dialog:Dialog

    //nav_bar
    lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_doctor)
        init()
        getDoctorsData()
        addDoctorBtn.setOnClickListener { addDoctorDialog()}

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
               filterList(newText)
                return true
            }
        })
        //nav_bar
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nau_dashboard->dashboard()
                R.id.nau_profile2-> adminProfile()
                R.id.nau_booking2-> requests()
                R.id.nau_doctor-> doctor()
                R.id.nau_logout2-> logout()
            }
            true
        }
    }
    //nav_bar
    private fun dashboard() {
        startActivity(Intent(this, Dashboard::class.java))
    }
    private fun logout() {
    }
    private fun requests() {
        startActivity(Intent(this, recycle_request::class.java))
    }
    private fun adminProfile() {
        startActivity(Intent(this, Profile::class.java))
    }
    private fun doctor() {
        startActivity(Intent(this, Doctors::class.java))
    }
    //nav_bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun filterList(query: String?) {
        if (query!=null){
            val filteredList=ArrayList<DoctorData>()
            for (i in doctorList){
                if ((i.firstName+" "+i.lastName).toLowerCase(Locale.ROOT).contains(query)){
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()){
                filteredList.clear()
                dAdapter.setFilteredList(filteredList)
                Toast.makeText(applicationContext,R.string.noDoctorFound,Toast.LENGTH_SHORT).show()
            }else{
                dAdapter.setFilteredList(filteredList)
            }
        }
    }
    private fun addDoctorDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_doctor)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val displayMetrics=DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val layoutParam=WindowManager.LayoutParams()
        layoutParam.copyFrom(dialog.window?.attributes)
        layoutParam.width=(displayMetrics.widthPixels *0.9F).toInt()
        layoutParam.height=(displayMetrics.heightPixels *0.9F).toInt()
        dialog.window?.attributes=layoutParam
        addBtn=dialog.findViewById(R.id.add_btn)
        cancelBtn=dialog.findViewById(R.id.cancelBtn)
        firstName=dialog.findViewById(R.id.firstName_d)
        lastName=dialog.findViewById(R.id.lastName_d)
        specialization=dialog.findViewById(R.id.specialization_d)
        birthDate=dialog.findViewById(R.id.birthdate_d)
        genderRadio=dialog.findViewById(R.id.gender_d)
        phone=dialog.findViewById(R.id.phone_d)
        email=dialog.findViewById(R.id.email_d)
        password=dialog.findViewById(R.id.password_d)
        birthDate.setOnClickListener { calenderShow() }
        cancelBtn.setOnClickListener { dialog.dismiss() }
        addBtn.setOnClickListener {
            if (checkInternet(this)){
               if(checkEmptyFields()){
                   var selectedGender:Int=genderRadio!!.checkedRadioButtonId
                   radioButton = dialog.findViewById(selectedGender)
                   checkPhone(phone)
               }
            } else {
                Toast.makeText(this, R.string.internetError, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun checkEmail(email:EditText){
        val query:Query=database.child("Doctors").orderByChild("email").equalTo(email.text.toString())
        query.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    email.setError(getText(R.string.emailFound))
                }else{
                    addDoctor(radioButton.text.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun addDoctor(gender:String) {
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val doctorId = auth.currentUser!!.uid
                    val doctor = DoctorData(doctorId,
                        firstName.text.toString(),
                        lastName.text.toString(),
                        birthDate.text.toString(),
                        specialization.text.toString(),
                        phone.text.toString(),
                        gender,
                        email.text.toString(),
                        password.text.toString()
                    )
                    database.child("Doctors").child(doctorId).setValue(doctor).addOnSuccessListener {
                        Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }.addOnFailureListener { err ->
                        Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                    }
                }
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
                birthDate.setText("$year" +"-"+ (monthOfYear + 1)+"-"+dayOfMonth  )
                dateEntered=LocalDate.of(year,month,dayOfMonth)
            }, year, month, day
        )
        datePickerDialog.show()
    }
    private fun checkEmptyFields():Boolean {
        var emailPattern = "[a-zA-Z0-9._-]+@ocs+\\.+[a-z]+"
        var phonePattern = "^01[0125][0-9]{8}\$"
        if (firstName.text.toString().isNotEmpty() && lastName.text.toString().isNotEmpty()
            && email.text.toString().trim().matches(emailPattern.toRegex())&& birthDate.text.toString().isNotEmpty()
            && specialization.text.toString().isNotEmpty() && phone.text.toString().isNotEmpty()
            && phone.text.toString().length == 11 && phone.text.toString().trim().matches(phonePattern.toRegex()) && password.text.toString().isNotEmpty()
            && password.text.toString().trim().length >= 8 && Period.between(dateEntered,currentDate).years >26
                ) {
            return true
        }else{
            if(email.text.toString().trim().isEmpty() && firstName.text.toString().isEmpty() && birthDate.text.toString().isEmpty()
                && password.text.toString().isEmpty() && specialization.text.toString().isEmpty()
                &&(lastName.text.toString().isEmpty() && phone.text.toString().isEmpty())){
                email.setError(getText(R.string.requird))
                firstName.setError(getText(R.string.requird))
                lastName.setError(getText(R.string.requird))
                phone.setError(getText(R.string.requird))
                birthDate.setError(getText(R.string.requird))
                password.setError(getText(R.string.requird))
                specialization.setError(getText(R.string.requird))
                Toast.makeText(this,R.string.emptyAllData,Toast.LENGTH_SHORT).show()
            }
            else if (email.text.toString().trim().isEmpty()){
                email.setError(getText(R.string.requird))
                Toast.makeText(this, R.string.emptyEmail, Toast.LENGTH_SHORT).show()
            } else if (!email.text.toString().trim().matches(emailPattern.toRegex())){
                email.setError(getText(R.string.notValidEmail))
                Toast.makeText(this, R.string.notValidEmail, Toast.LENGTH_SHORT).show()
            }
            else if (firstName.text.toString().isEmpty()){
                firstName.setError(getText(R.string.requird))
                Toast.makeText(this, R.string.emptyFName, Toast.LENGTH_SHORT).show()
            }
            else if (lastName.text.toString().isEmpty()){
                lastName.setError(getText(R.string.requird))
                Toast.makeText(this, R.string.emptyLName, Toast.LENGTH_SHORT).show()
            }
            else if (phone.text.toString().isEmpty()){
                phone.setError(getText(R.string.requird))
                Toast.makeText(this, R.string.emptyPhone, Toast.LENGTH_SHORT).show()
            }
            else if (phone.text.toString().length != 11 || !phone.text.toString().trim().matches(phonePattern.toRegex())){
                phone.setError(getText(R.string.notValidNumber))
                Toast.makeText(this, R.string.notValidNumber, Toast.LENGTH_SHORT).show()
            }
            else if (specialization.text.toString().isEmpty()){
                specialization.setError(getText(R.string.requird))
                Toast.makeText(this,R.string.emptySpecialization,Toast.LENGTH_SHORT)
            }
            else if (birthDate.text.toString().isEmpty()) {
                birthDate.setError(getText(R.string.requird))
                Toast.makeText(applicationContext,R.string.emptyBirthDate,Toast.LENGTH_LONG).show()
            }else if (Period.between(dateEntered,currentDate).years <15){
                birthDate.setError(getText(R.string.doctorAgeError))
                Toast.makeText(applicationContext,R.string.doctorAgeError,Toast.LENGTH_LONG).show()
            }
            else if (password.text.toString().isEmpty()) {
                password.setError(getText(R.string.requird))
                Toast.makeText(applicationContext, R.string.emptyPassword, Toast.LENGTH_LONG).show()
            } else if (password.text.toString().trim().length < 8) {
                password.setError(getText(R.string.notValidPassword))
                Toast.makeText(applicationContext, R.string.notValidPassword, Toast.LENGTH_LONG).show()
            }
            return false
        }
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
    private fun init(){
        doctorRecycle = findViewById(R.id.recycleview1)
        doctorRecycle.layoutManager = LinearLayoutManager(this)
        doctorRecycle.setHasFixedSize(true)
        doctorList = arrayListOf<DoctorData>()
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        searchView=findViewById(R.id.searchDoctor)
        auth = FirebaseAuth.getInstance()
        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addDoctorBtn=findViewById(R.id.addingDoctorBtn)
        dialog=Dialog(this)
}
    //listener
    override fun onClick(c: ServiceData?) {
        val toast = Toast.makeText(applicationContext, c?.serviceImage!!, Toast.LENGTH_LONG)
        toast.show()
    }
    private fun checkPhone(phone:EditText){
        val queryPhone: Query =database.child("Doctors").orderByChild("phone").equalTo(phone.text.toString())
        queryPhone.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    phone.setError(getText(R.string.phoneFound))
                    Toast.makeText(baseContext,R.string.phoneFound,Toast.LENGTH_LONG).show()
                }else{
                    checkEmail(email)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun getDoctorsData(){
        //dAdapter.clearList()
    database.child("Doctors").addValueEventListener(object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
           doctorList.clear()
            if (snapshot.exists()){
                for (docData in snapshot.children){
                    val docName=docData.getValue(DoctorData::class.java)
                    doctorList.add(docName!!)
                }
                dAdapter= DoctorAdapter(doctorList)
                doctorRecycle.adapter=dAdapter
            }
        }
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })

}

}