package com.example.ocs.Intro.patient.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.example.ocs.R

class BookAppointment : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var serviceSpinner:Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appoitnment)
        bindingItems()
    }
    private fun bindingItems(){
       serviceSpinner=findViewById(R.id.service_spinner)
    }
    private fun initializeSpinner(){
        var selectedService:ArrayList<String> = ArrayList()
        selectedService.add(R.string.slider2_title.toString())
        selectedService.add(R.string.slider3_title.toString())
        selectedService.add(R.string.clinic_service.toString())

        serviceSpinner.onItemSelectedListener=this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}