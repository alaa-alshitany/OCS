package com.example.ocs.Admin.Dashboard

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.ocs.Admin.Appointments.Appointments
import com.example.ocs.Admin.Doctors.Doctors
import com.example.ocs.Admin.Profile
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.R
import com.example.ocs.databinding.ActivityDashboardBinding
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

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
        /**val chart = findViewById<BarChart>(R.id.chart)
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
        chart.invalidate()**/


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


    private fun createHorizontalBarChart(labels: List<String>, counts: List<Int>) {
        val barEntries = mutableListOf<BarEntry>()

        // Add each bar entry to the list
        for (i in labels.indices) {
            barEntries.add(BarEntry(i.toFloat(), counts[i].toFloat()))
        }

        // Set up the bar data set
        val barDataSet = BarDataSet(barEntries, "Counts")

        // Set a random color for each bar
        val colors = mutableListOf<Int>()
        for (i in labels.indices) {
            colors.add(getRandomColor())
        }
        barDataSet.colors = colors

        // Set up the x-axis labels and position
        val xAxisLabels = labels.toTypedArray()

        // Define a subclass of ValueFormatter that overrides the getFormattedValue() method with a lambda function
        class LabelValueFormatter(private val labels: List<String>) : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return (value.toInt() + 1).toString()
            }
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return value.toInt().toString()
            }
        }

        // Use the LabelValueFormatter subclass to set the value formatter for the y-axis labels
        val yAxisLeft = YAxis().apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            textSize = 16f
            setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
            setDrawLabels(true)
            valueFormatter = LabelValueFormatter(labels)
            axisMinimum = 1f // set minimum value to 1
            axisMaximum = Math.ceil(counts.maxOrNull()?.toDouble() ?: 0.0).toFloat() // set maximum value to highest value rounded up to the nearest whole number
            granularity = 1f // set interval to 1
        }


        val yAxisRight = YAxis().apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            textSize = 16f
            setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
            setDrawLabels(false)
            valueFormatter = LabelValueFormatter(labels)
        }

        // Set up the bar data object
        val barData = BarData(barDataSet).apply {
            barWidth = 0.5f
        }

        // Set up the chart view
        val barChart = findViewById<HorizontalBarChart>(R.id.chart)
        barChart.apply {
            data = barData
            setDrawGridBackground(false)
            setDrawBorders(false)
            setScaleEnabled(false)
            setTouchEnabled(false)
            setPinchZoom(false)
            setDoubleTapToZoomEnabled(false)
            setDrawValueAboveBar(false)
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(xAxisLabels)
                position = XAxis.XAxisPosition.BOTTOM_INSIDE
                setDrawGridLines(false)
                setDrawAxisLine(false)
               granularity = 1f
                isGranularityEnabled = true
            }

            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT)
            //axisLeft = yAxisLeft
            axisRight.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = true
            description.text="Doctor's Specializations"
            description.textSize= 10F
            description.xOffset=100f
            description.yOffset=-14f
            setExtraOffsets(120f, 0f, 0f, 0f)
            animateXY(1000, 1000)
            invalidate()
        }
    }
    // Helper function to generate a random color
    private fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
     private fun init() {
         context = this
         pref = Prefrences(context)
         val database = FirebaseDatabase.getInstance()
         val dataRef = database.getReference("Doctors")

         dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 val specializationCountMap = mutableMapOf<String, Int>()

                 for (doctorSnapshot in snapshot.children) {
                     val specialization = doctorSnapshot.child("specialization").value.toString()
                     specializationCountMap[specialization] = specializationCountMap.getOrDefault(specialization, 0) + 1
                 }

                 // Here, you can use the specializationCountMap to create your horizontal bar chart
                 createHorizontalBarChart(specializationCountMap.keys.toList(), specializationCountMap.values.toList())
             }

             override fun onCancelled(error: DatabaseError) {
                 Log.e(TAG, "Failed to read value.", error.toException())
             }
         })
     }
    private fun moveToLogin() {
        startActivity(Intent(this, Login::class.java).putExtra("hint", R.string.a_email_hint.toString()))
        Toast.makeText(this, R.string.logout, Toast.LENGTH_LONG).show()
        finish()
    }

}