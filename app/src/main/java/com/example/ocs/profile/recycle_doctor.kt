package com.example.ocs.profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R

class recycle_doctor : AppCompatActivity() {

    private lateinit var recycleView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    lateinit var imageList:Array<Int>
    lateinit var titleList:Array<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_doctor)

        imageList = arrayOf(
            R.drawable.baseline_user_24,
            R.drawable.baseline_user_24,
            R.drawable.baseline_user_24,
        )

        titleList = arrayOf(
            "DR/ Mohammed A hmed",
            "DR/ Ahmed Ali",
            "DR/ Ali Naser"
        )


        recycleView = findViewById(R.id.recycleview1)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)
        dataList = arrayListOf<DataClass>()
        getData()


    }

    private fun getData(){
        for(i in imageList.indices){
            val dataClass = DataClass(imageList[i], titleList[i])
            dataList.add(dataClass)
        }
        recycleView.adapter = AdapterClass(dataList)
    }

}