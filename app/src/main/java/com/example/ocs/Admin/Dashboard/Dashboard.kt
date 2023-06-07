package com.example.ocs.Admin.Dashboard

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class Dashboard : AppCompatActivity()  {
    lateinit var piechart: PieChart
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var pref: Prefrences
    private lateinit var context: Context
    private lateinit var barChart:HorizontalBarChart
    private lateinit var dView : View
    private lateinit var adminName:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.elevation= 0F
        init()
        barchartData()
        //listener on dashboard_card
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

private fun barchartData(){
    val database = FirebaseDatabase.getInstance()
    val dataRef = database.getReference("Doctors")

    dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val specializationCountMap = mutableMapOf<String, Int>()

            for (doctorSnapshot in snapshot.children) {
                val specialization = doctorSnapshot.child("specialization").value.toString()
                specializationCountMap[specialization] = specializationCountMap.getOrDefault(specialization, 0) + 1
            }
            createHorizontalBarChart(specializationCountMap.keys.toList(), specializationCountMap.values.toList())
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e(TAG, "Failed to read value.", error.toException())
        }
    })
}
    @SuppressLint("ResourceAsColor")
    private fun createHorizontalBarChart(labels: List<String>, counts: List<Int>) {
        barChart.visibility=View.VISIBLE
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
        val xAxis = barChart.xAxis
        xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(xAxisLabels)
            position = XAxis.XAxisPosition.BOTTOM_INSIDE
            setDrawGridLines(false)
            setDrawAxisLine(false)
            granularity = 1f
            textSize = 10f
            isGranularityEnabled = true
            labelCount = labels.size // Set the number of labels to show
            setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return labels[value.toInt()]
                }
            })
        }
            val extraLeftOffset = resources.getDimensionPixelSize(R.dimen.bar_chart_extra_left_offset)
            val extraRightOffset = resources.getDimensionPixelSize(R.dimen.bar_chart_extra_right_offset)
            barChart.setExtraOffsets(extraLeftOffset.toFloat(), 0f, extraRightOffset.toFloat(), 0f)

        // Define a subclass of ValueFormatter that overrides the getFormattedValue() method with a lambda function
        class LabelValueFormatter(private val labels: List<String>) : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return value.toInt().toString()
            }
        }

        // Use the LabelValueFormatter subclass to set the value formatter for the y-axis labels
        var yAxisLeft = YAxis().apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            textSize = 16f
            setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
            setDrawLabels(true)
            valueFormatter = LabelValueFormatter(labels)
            axisMinimum = 0f // set minimum value to 0
            axisMaximum = Math.ceil(counts.maxOrNull()?.toDouble() ?: 0.0).toInt().toFloat() // set maximum value to highest value rounded up to the nearest whole number
            granularity = 1f // set interval to 1
        }
        val yAxisRight = YAxis().apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            textSize = 10f
            setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
            setDrawLabels(false)
            valueFormatter = LabelValueFormatter(labels)
        }

        // Set up the bar data object
        val barData = BarData(barDataSet).apply {
            barWidth = 0.5f
        }

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
            barChart.axisLeft.mAxisMinimum=0f
            barChart.axisLeft.granularity=1f
            barChart.axisLeft.mAxisRange=8f
            axisRight.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = true
            description.text="Doctor's Specializations"
            description.textSize= 10F
            description.xOffset=100f
            description.yOffset=-14f
            animateXY(1000, 1000)
            invalidate()
        }
    }
    // Helper function to generate a random color
    private fun getRandomColor(): Int {
        val rnd = Random()
        val minBrightness = 150 // Set the minimum brightness to 150
        val minSaturation = 0.4f // Set the minimum saturation to 0.4
        var r = rnd.nextInt(256)
        var g = rnd.nextInt(256)
        var b = rnd.nextInt(256)
        var hsv = FloatArray(3)
        Color.RGBToHSV(r, g, b, hsv)
        var brightness = hsv[2] * 255
        var saturation = hsv[1]
        while (brightness < minBrightness || saturation < minSaturation) {
            // Generate new random values until the color meets the brightness and saturation criteria
            r = rnd.nextInt(256)
            g = rnd.nextInt(256)
            b = rnd.nextInt(256)
            Color.RGBToHSV(r, g, b, hsv)
            brightness = hsv[2] * 255
            saturation = hsv[1]
        }
        return Color.argb(200, r, g, b)
    }
    private fun init() {
        context = this
        pref = Prefrences(context)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        barChart = binding.chart
        barChart.visibility=View.INVISIBLE
        dView = findViewById(R.id.textView_dash)
        adminName=findViewById(R.id.adminNameTxt)
        adminName.setText("Admin: ${pref.userName}")
    }
    private fun moveToLogin() {
        startActivity(Intent(this, Login::class.java).putExtra("hint", R.string.a_email_hint.toString()))
        Toast.makeText(this, R.string.logout, Toast.LENGTH_LONG).show()
        finish()
    }

}