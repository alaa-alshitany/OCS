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

class recycle_book : AppCompatActivity() {
    private lateinit var recycleView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass2>
    lateinit var TitleList:Array<String>
    lateinit var DetailList:Array<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_book)

        TitleList = arrayOf(
            " Mohammed Ahmed",
            " Ahmed Ali",
            " Ali Naser"
        )

        DetailList = arrayOf(
            " 2-5-2023",
            " 3-6-2023",
            " 7-7-2023"
        )


        recycleView = findViewById(R.id.recycleview2)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)
        dataList = arrayListOf<DataClass2>()
        getData()



    }


    private fun getData(){
        for(i in TitleList.indices){
            val dataClass = DataClass2(TitleList[i], DetailList[i])
            dataList.add(dataClass)
        }
        recycleView.adapter = AdapterClass2(dataList)
    }

}
