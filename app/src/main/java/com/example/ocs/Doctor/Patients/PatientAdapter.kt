package com.example.ocs.Doctor

import com.example.ocs.Doctor.OnPatientListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.Patient.PatientData
import com.example.ocs.R

class PatientAdapter( var patientList: ArrayList<PatientData>, private val listener: OnPatientListener):RecyclerView.Adapter<PatientAdapter.ViewHolderClass>() {

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientName: TextView = itemView.findViewById(R.id.doctorName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.doctor_card, parent, false)
        return ViewHolderClass(v)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return patientList.size
    }

    fun setFilteredList(list: ArrayList<PatientData>) {
        this.patientList = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = patientList[position]
        holder.patientName.text = "${currentItem.firstName} ${currentItem.lastName}"
        holder.itemView.setOnClickListener {
            listener.onClick(currentItem)
        }
    }
}