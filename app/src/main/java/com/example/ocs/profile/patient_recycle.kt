package com.example.ocs.profile

import android.annotation.SuppressLint
import android.icu.text.DateTimePatternGenerator.PatternInfo.OK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R
import com.example.ocs.model.patientData
import com.example.ocs.view.PatientAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class patient_recycle : AppCompatActivity() {
    private  lateinit var addBtn:FloatingActionButton
    private lateinit var recv:RecyclerView
    private lateinit var patientList:ArrayList<patientData>
    private lateinit var patientAdapter: PatientAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_recycle)



        //patient_recycle
        patientList = ArrayList()
        addBtn = findViewById(R.id.addingBtn)
        recv = findViewById(R.id.pRecycler)
        patientAdapter = PatientAdapter(this, patientList)
        recv.layoutManager = LinearLayoutManager(this)
        recv.adapter = patientAdapter
        addBtn.setOnClickListener {
            (addInfo())}

    }
    private fun addInfo() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_item, null)

        val patientName = v.findViewById<EditText>(R.id.patientName)
        val addDialog = AlertDialog.Builder(this)
        val view = addDialog.setView(v)
        addDialog.setPositiveButton("OK",{
                dialog,
                id->finish()
            val names = patientName.text.toString()
            patientList.add(patientData("Name: $names"))
            patientAdapter.notifyDataSetChanged()
            Toast.makeText(this, "Adding patient success",Toast.LENGTH_SHORT).show()
            dialog.dismiss()

        })



        addDialog.setNegativeButton("Cancel",{
                dialog ,
                id->finish()
            dialog.dismiss()
            Toast.makeText(this, "Cancel",Toast.LENGTH_SHORT).show()
        })
        addDialog.create()
        addDialog.show()
    }


}