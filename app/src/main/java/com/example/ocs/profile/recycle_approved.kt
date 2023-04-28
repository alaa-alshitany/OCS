package com.example.ocs.profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R

class recycle_approved : AppCompatActivity() {
    private lateinit var recycleView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass_approv>
    lateinit var TitleList:Array<String>
    lateinit var phoneList:Array<String>
    lateinit var serveList:Array<String>
    lateinit var dateList:Array<String>
    lateinit var timeList:Array<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_approved)

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


    }

    private fun getData(){
        for(i in TitleList.indices){
            val dataClass = DataClass_approv(TitleList[i], phoneList[i],serveList[i],dateList[i],timeList[i])
            dataList.add(dataClass)
        }
        recycleView.adapter = AdapterClass4(dataList)
    }

}
