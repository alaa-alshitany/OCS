package com.example.ocs.Intro.patient.services

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R

class services : AppCompatActivity() {
    private lateinit var intent2: Intent
    private lateinit var fullName:String
    private lateinit var list: RecyclerView
    private fun bindingItems() {
       list=findViewById(R.id.recyclerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        bindingItems()
        // this creates a vertical layout Manager
        list.layoutManager=LinearLayoutManager(this)

        // ArrayList of class serviceViewModel
        val data = ArrayList<serviceModel>(4)
        data.add(serviceModel(R.drawable.rays_service,R.string.slider2_title))
        data.add(serviceModel(R.drawable.booking,R.string.book_appointment_title))
        data.add(serviceModel(R.drawable.tests_service,R.string.slider3_title))
        data.add(serviceModel(R.drawable.clinics_service,R.string.clinic_service))


        //This will pass the ArrayList to our Adapter
        val adapter = serviceAdapter(this,data)

        // Setting the Adapter with the recyclerview
        list.adapter = adapter
        getIntentExtra()
        //adapter.setOnItem


    }
    private fun getIntentExtra(){
        intent2=intent
        fullName=intent2.getStringExtra("name").toString()
    }

}