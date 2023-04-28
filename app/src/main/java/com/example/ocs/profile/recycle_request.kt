package com.example.ocs.profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R

class recycle_request : AppCompatActivity() {
    private lateinit var recycleView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass5>
    lateinit var TitleList:Array<String>
    lateinit var phoneDetail:Array<String>
    lateinit var serveDetail:Array<String>
    lateinit var imageList:Array<Int>
    lateinit var imageList2:Array<Int>

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
        dataList = arrayListOf<DataClass5>()
        getData()


    }

    private fun getData(){
        for(i in TitleList.indices){
            val dataClass = DataClass5(TitleList[i],phoneDetail[i],serveDetail[i],imageList[i],imageList2[i])
            dataList.add(dataClass)
        }
        recycleView.adapter = AdapterClass5(dataList)
    }

}