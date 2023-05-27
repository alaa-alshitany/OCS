package com.example.ocs.Admin.Dashboard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ocs.Admin.Appointments.Appointments
import com.example.ocs.Admin.Doctors.Doctors
import com.example.ocs.Admin.Profile
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.R
//import com.example.ocs.Admin.profile
import com.example.ocs.databinding.ActivityDashboardBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class Dashboard : AppCompatActivity()  {

    lateinit var piechart: PieChart

    //listener on dashboard_card
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var pref: Prefrences
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.elevation= 0F
        init()
        //listener on dashboard_card
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profile.setOnClickListener {
            val cardProfileIntent = Intent(this, Profile::class.java)
            startActivity(cardProfileIntent)
        }
        binding.appointments.setOnClickListener {
            val cardAppointmentIntent = Intent(this, Appointments::class.java)
            startActivity(cardAppointmentIntent)
        }
        binding.doctors.setOnClickListener {
            val cardDoctorsIntent = Intent(this, Doctors::class.java)
            startActivity(cardDoctorsIntent)
        }
        binding.logout.setOnClickListener {
        pref.prefClear()
            moveToLogin()
        }

      //bar_chart
        val chart = findViewById<BarChart>(R.id.chart)
        val entries = listOf(
            BarEntry(0f, 10f),
            BarEntry(1f, 20f),
            BarEntry(2f, 30f),
            BarEntry(3f, 40f),
            BarEntry(4f, 50f),
            BarEntry(5f, 60f)
        )
        val dataSet = BarDataSet(entries, "Values")
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val data = BarData(dataSet)
        chart.data = data
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        val labels = arrayOf("Neuro", "pediatric", "Gynecologic", "Hematologists", "Thoracic", "Urologic")
        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        chart.invalidate()


        //piechart
        piechart=findViewById(R.id.pie_chart)
        val list1: ArrayList<PieEntry> = ArrayList()
        list1.add(PieEntry(100f, "Requests"))
        list1.add(PieEntry(101f, "Approved"))

        val pieDataSet= PieDataSet(list1, "List")
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        pieDataSet.valueTextSize=8f
        pieDataSet.valueTextColor= Color.BLACK

        val pieData= PieData(pieDataSet)
        piechart.data=pieData
        piechart.description.text="Pie Chart"
        piechart.centerText= "List"
        piechart.animateY(2000)



    }
private fun init(){
    context=this
    pref= Prefrences(context)
}
    private fun moveToLogin() {
        startActivity(Intent(this, Login::class.java).putExtra("hint", R.string.a_email_hint.toString()))
        Toast.makeText(this, R.string.logout, Toast.LENGTH_LONG).show()
        finish()
    }

}