package com.example.ocs.Admin.Appointments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R
import com.example.ocs.Patient.booking.AppointmentData

class AppointmentAdapter (private var dataList: ArrayList<AppointmentData>):
    RecyclerView.Adapter<AppointmentAdapter.ViewHolderClass>() {
    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView){
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val patientPhone:TextView=itemView.findViewById(R.id.patientPhone)
        val service:TextView=itemView.findViewById(R.id.patientService)
        val acceptBtn:ImageButton=itemView.findViewById(R.id.accept)
        val cancelBtn:ImageButton=itemView.findViewById(R.id.cancel)
        init {
            acceptBtn.setOnClickListener {  }
            cancelBtn.setOnClickListener {  }
        }

    }
        private fun acceptRequest(){

        }
    private fun cancelRequest(){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.appointment_card, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: AppointmentAdapter.ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.patientName.text =currentItem.patientName
        holder.patientPhone.text=currentItem.phoneNumber
        holder.service.text=currentItem.serviceType
    }
}