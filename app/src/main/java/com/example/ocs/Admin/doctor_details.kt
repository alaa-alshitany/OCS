/*package com.example.ocs.Admin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ocs.Patient.Profile.Profile
import com.example.ocs.R
import com.example.ocs.Admin.Dashboard.Dashboard
import com.example.ocs.Admin.Doctors.Doctors
import com.google.android.material.navigation.NavigationView

class doctor_details: AppCompatActivity() {

    //nav_bar
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.doctor_details)

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
}*/