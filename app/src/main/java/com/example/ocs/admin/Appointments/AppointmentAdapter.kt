package com.example.ocs.admin.Appointments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R
import com.example.ocs.admin.Doctors.DoctorAdapter
import com.example.ocs.patient.booking.AppointmentData

class AppointmentAdapter (private var dataList: ArrayList<AppointmentData>):
    RecyclerView.Adapter<DoctorAdapter.ViewHolderClass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorAdapter.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doctor_card, parent, false)
        return DoctorAdapter.ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: DoctorAdapter.ViewHolderClass, position: Int) {
        TODO("Not yet implemented")
    }
}