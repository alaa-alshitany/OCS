package com.example.ocs.Intro.doctor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R

class PatientAdapter(val c:Context, val patientList: ArrayList<patientData>):RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    inner class PatientViewHolder(val v: View): RecyclerView.ViewHolder(v){
        val name = v.findViewById<TextView>(R.id.pTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_item,parent,false)
        return PatientViewHolder(v)
    }

    override fun getItemCount(): Int {
        return patientList.size
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val newList = patientList[position]
        holder.name.text = newList.PatientName
    }
}