package com.example.ocs.Doctor.Model

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.*
import com.example.ocs.Admin.Doctors.DoctorData
import com.example.ocs.Doctor.PatientAdapter
import com.example.ocs.Patient.PatientData
import com.example.ocs.R
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_files)
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
        patientNamesSpinner=findViewById(R.id.patientsSpinner)
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        patientNamesSpinner.adapter = adapter
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
    }
}