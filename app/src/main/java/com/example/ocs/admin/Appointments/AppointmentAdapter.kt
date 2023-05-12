package com.example.ocs.admin.Appointments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R
import com.example.ocs.Patient.booking.AppointmentData

class AppointmentAdapter (private var dataList: ArrayList<AppointmentData>):
    RecyclerView.Adapter<AppointmentAdapter.ViewHolderClass>() {
    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentAdapter.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doctor_card, parent, false)
        return AppointmentAdapter.ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: AppointmentAdapter.ViewHolderClass, position: Int) {
        TODO("Not yet implemented")
    }
}