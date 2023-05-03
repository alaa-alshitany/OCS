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
import com.example.ocs.patient.Profile.Profile
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView

class recycle_request : AppCompatActivity() {
    private lateinit var recycleView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass_req>
    lateinit var TitleList:Array<String>
    lateinit var phoneDetail:Array<String>
    lateinit var serveDetail:Array<String>
    lateinit var imageList:Array<Int>
    lateinit var imageList2:Array<Int>

    //nav_bar
    lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_request)

        TitleList = arrayOf(
            " Mohammed Ahmed",
            " Ahmed Ali",
            " Ali Naser"
        )

        phoneDetail = arrayOf(
            " 01025638952",
            " 01236528971",
            " 01238569411"
        )
        serveDetail = arrayOf(
            " medical rays",
            " medical test",
            " medical clinic"
        )
        imageList = arrayOf(
            R.drawable.baseline_check_green,
            R.drawable.baseline_check_green,
            R.drawable.baseline_check_green,
        )
        imageList2 = arrayOf(
            R.drawable.baseline_wrong,
            R.drawable.baseline_wrong,
            R.drawable.baseline_wrong,
        )



        recycleView = findViewById(R.id.recycleView5)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)
        dataList = arrayListOf<DataClass_req>()
        getData()

        //nav_bar
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nau_profile2-> adminProfile()
                R.id.nau_booking2-> requests()
                R.id.nau_doctor-> doctordetails()
                R.id.nau_logout2-> logout()
            }

            true
        }


    }
    //nav_bar
    private fun logout() {
    }

    private fun requests() {
        startActivity(Intent(this, recycle_request::class.java))
    }

    private fun adminProfile() {
        startActivity(Intent(this, Profile::class.java))
    }

    private fun doctordetails() {
        startActivity(Intent(this, doctor_details::class.java))
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
            val dataClass = DataClass_req(TitleList[i],phoneDetail[i],serveDetail[i],imageList[i],imageList2[i])
            dataList.add(dataClass)
        }
        recycleView.adapter = AdapterClass_req(dataList)
    }
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }

}