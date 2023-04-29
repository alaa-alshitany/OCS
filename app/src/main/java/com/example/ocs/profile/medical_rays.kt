package com.example.ocs.profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView

class medical_rays : AppCompatActivity() {
    private lateinit var recycleView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass6>
    lateinit var serviceList:Array<String>
    lateinit var priceList:Array<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_rays)

        serviceList = arrayOf(
            " CT",
            " MRI",
            " Ultrasound",
            "X-rays"

        )

        priceList = arrayOf(
            " 440LE",
            " 950LE",
            " 850LE",
            " 300LE"
        )


        recycleView = findViewById(R.id.recycleview2)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)
        dataList = arrayListOf<DataClass6>()
        getData()



    }


    private fun getData(){
        for(i in serviceList.indices){
            val dataClass = DataClass6(serviceList[i], priceList[i])
            dataList.add(dataClass)
        }
        recycleView.adapter = AdapterClass6(dataList)
    }

}