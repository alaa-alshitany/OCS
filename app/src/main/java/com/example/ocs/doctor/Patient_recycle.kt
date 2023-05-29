package com.example.ocs.doctor

import android.annotation.SuppressLint
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class Patient_recycle : AppCompatActivity() {
    private  lateinit var addBtn:FloatingActionButton
    private lateinit var recv:RecyclerView
    private lateinit var patientList:ArrayList<patientData>
    private lateinit var patientAdapter: PatientAdapter

    //nav_bar
    lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_recycle)

        //nav_bar
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nau_profile1-> Toast.makeText(applicationContext,"clicked Profile", Toast.LENGTH_SHORT).show()
                R.id.nau_booking1-> Toast.makeText(applicationContext,"clicked Booking", Toast.LENGTH_SHORT).show()
                R.id.nau_patient-> Toast.makeText(applicationContext,"clicked patient", Toast.LENGTH_SHORT).show()
                R.id.nau_model-> Toast.makeText(applicationContext,"clicked Home", Toast.LENGTH_SHORT).show()
                R.id.nau_logout1-> Toast.makeText(applicationContext,"clicked Logout", Toast.LENGTH_SHORT).show()
            }

            true
        }




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

    //nav_bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}