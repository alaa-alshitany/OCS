package com.example.ocs.Doctor.Model

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ocs.Admin.Doctors.DoctorData
import com.example.ocs.Doctor.Appointments.Appointments
import com.example.ocs.Doctor.DoctorProfile.Profile
import com.example.ocs.Doctor.PatientAdapter
import com.example.ocs.Doctor.Patients
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.Patient.PatientData
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage

class UploadFiles : AppCompatActivity() {
    private lateinit var patientNamesSpinner: Spinner
    private lateinit var geneExp:Button
    private lateinit var geneMut:Button
    private lateinit var geneMeth:Button
    private lateinit var adapter:ArrayAdapter<String>
    private lateinit var patientName:String
    val database = FirebaseDatabase.getInstance().reference
    val storageRef = FirebaseStorage.getInstance().getReference("Test_Data/Patient_Data")
    private lateinit var progressBar1:ProgressBar
    private lateinit var progressBar2:ProgressBar
    private lateinit var progressBar3:ProgressBar
    private lateinit var progress: ProgressBar

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var pref: Prefrences
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var context: Context
    private lateinit var navHeader : View
    private lateinit var userName: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_files)
        supportActionBar!!.elevation=0F
        init()
        geneExp.setOnClickListener {
            patientName=patientNamesSpinner.selectedItem.toString()
            launchFilePicker(100,progressBar1)
        }
        geneMut.setOnClickListener {
            patientName=patientNamesSpinner.selectedItem.toString()
            launchFilePicker(101,progressBar2)
        }
        geneMeth.setOnClickListener {
            patientName=patientNamesSpinner.selectedItem.toString()
            launchFilePicker(102,progressBar3)
        }

        //nav_bar
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nau_profile1 -> doctorProfile()
                R.id.nau_booking1 -> appointment()
                R.id.nau_patient -> patients()
                R.id.nau_model -> model()
                R.id.nau_logout1 -> logout()
            }

            true
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
    private fun launchFilePicker(requestCode: Int, progressBar: ProgressBar) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "text/csv" // Set the MIME type of the file you want to select
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, requestCode)
        progressBar.visibility = View.VISIBLE
    }
    @SuppressLint("Range")
    private fun getFileNameFromUri(uri: Uri?): String {
        val cursor = contentResolver.query(uri!!, null, null, null, null)
        cursor!!.moveToFirst()
        val fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor.close()
        return fileName
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val progressBar: ProgressBar // Declare a progress bar object
        val button:Button
        // Determine which progress bar to update based on the request code
        when (requestCode) {
            100 -> progressBar = progressBar1
            101 -> progressBar = progressBar2
            102 -> progressBar = progressBar3
            else -> return
        }
        when (requestCode) {
            100 -> button = geneExp
            101 -> button = geneMut
            102 -> button = geneMeth
            else -> return
        }

        if (resultCode == Activity.RESULT_OK) {
            // Get the file URI from the result
            val fileUri =data?.data

            // Create a storage reference to the file you want to upload
            val fileName = getFileNameFromUri(fileUri) // Implement this function to get the file name from the file URI
            val fileRef = storageRef.child(patientName).child(fileName)

            // Upload the file to Firebase Storage
            val uploadTask = fileRef.putFile(fileUri!!)

            // Monitor the upload progress using a progress listener
            uploadTask.addOnProgressListener { taskSnapshot ->
                // Update the UI with the upload progress
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                progressBar.progress = progress.toInt()
                //Toast.makeText(this, "${fileName}: Upload is $progress% done",Toast.LENGTH_SHORT).show()
            }
            // Handle any errors that occur during the upload process
            uploadTask.addOnFailureListener { exception ->
                // Handle the failure
                Toast.makeText(this, "${fileName}: Upload failed: ${exception.message}",Toast.LENGTH_SHORT).show()
            }

            uploadTask.addOnSuccessListener { taskSnapshot ->
                progressBar.progress = progressBar.max
                progressBar.visibility = View.GONE
                button.setText(R.string.uploaded)
                // Handle successful uploads
               Toast.makeText(this, "${fileName}: Upload successful",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun init(){
        progress=findViewById(R.id.progress_bar)
        progress.visibility=View.VISIBLE
        patientNamesSpinner=findViewById(R.id.patientsSpinner)
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        patientNamesSpinner.adapter = adapter
        progress=findViewById(R.id.progress_bar)
        database.child("Patients").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.clear()
                if (snapshot.exists()){
                    for (patientData in snapshot.children){
                        var patient = patientData.getValue<PatientData>()
                        val patientName="${patient?.firstName} ${patient?.lastName}"
                        adapter.add(patientName)
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        geneExp=findViewById(R.id.geneExpBtn)
        geneMut=findViewById(R.id.geneMutBtn)
        geneMeth=findViewById(R.id.geneMethBtn)
        progressBar1=findViewById(R.id.geneExpProgress)
        progressBar2=findViewById(R.id.geneMutProgress)
        progressBar3=findViewById(R.id.geneMethProgress)
        progressBar1.visibility=View.INVISIBLE
        progressBar2.visibility=View.INVISIBLE
        progressBar3.visibility=View.INVISIBLE

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        navHeader=navView.getHeaderView(0)
        context=this
        pref= Prefrences(context)
        userName=navHeader.findViewById(R.id.user_name)
        userName.setText(pref.userName)
        progress.visibility=View.GONE
    }
}