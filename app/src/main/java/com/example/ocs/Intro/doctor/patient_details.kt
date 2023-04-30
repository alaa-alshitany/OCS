package com.example.ocs.Intro.doctor

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView

class patient_details: AppCompatActivity() {

    //nav_bar
    lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.patient_details)

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
    }
    //nav_bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
