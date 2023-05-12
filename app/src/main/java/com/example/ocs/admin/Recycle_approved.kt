package com.example.ocs.admin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.Patient.Profile.Profile
import com.example.ocs.R
import com.example.ocs.admin.Doctors.Doctors
import com.google.android.material.navigation.NavigationView

class recycle_approved : AppCompatActivity() {
    private lateinit var recycleView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass_approv>
    lateinit var TitleList:Array<String>
    lateinit var phoneList:Array<String>
    lateinit var serveList:Array<String>
    lateinit var dateList:Array<String>
    lateinit var timeList:Array<String>

    //nav_bar
    lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_approved)
        supportActionBar!!.elevation= 0F

        TitleList = arrayOf(
            " Mohammed Ahmed",
            " Ahmed Ali",
            " Ali Naser"
        )

        phoneList = arrayOf(
            " 01208628016",
            " 01122520035",
            " 01023569821"
        )
        serveList = arrayOf(
            " Medical Test",
            " Medical Rays",
            " Medical Clinic"
        )
        dateList = arrayOf(
            " 22-6-2023",
            " 1-7-2023",
            " 20-8-2023"
        )
        timeList = arrayOf(
            " 9:00pm",
            " 10:30pm",
            "12:00pm"
        )

        recycleView = findViewById(R.id.recycleView4)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)
        dataList = arrayListOf<DataClass_approv>()
        getData()

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

    private fun getData(){
        for(i in TitleList.indices){
            val dataClass = DataClass_approv(TitleList[i], phoneList[i],serveList[i],dateList[i],timeList[i])
            dataList.add(dataClass)
        }
        recycleView.adapter = AdapterClass_approv(dataList)
    }
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
}
