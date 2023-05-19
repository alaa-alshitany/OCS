package com.example.ocs.Admin.Appointments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.Patient.booking.AppointmentData
import com.example.ocs.R

class ApprovedAdapter (var context: Context, private var dataList: ArrayList<AppointmentData>, private var doctorList:MutableMap<String,String>):
    RecyclerView.Adapter<ApprovedAdapter.ViewHolderClass>() {

    class ViewHolderClass  (itemView: View): RecyclerView.ViewHolder(itemView) {
        val patientName: TextView = itemView.findViewById(R.id.patientApprovedName)
        val patientPhone: TextView =itemView.findViewById(R.id.patientApprovedPhone)
        val service: TextView =itemView.findViewById(R.id.patientApprovedService)
        val date: TextView =itemView.findViewById(R.id.appointmentDate)
        val time: TextView =itemView.findViewById(R.id.appointmentTime)
        val doctorName:TextView=itemView.findViewById(R.id.appointmentDoctor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.appointment_approved_card, parent, false)
        return ApprovedAdapter.ViewHolderClass(itemView)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.patientName.text =currentItem.patientName
        holder.patientPhone.text=currentItem.phoneNumber
        holder.service.text=currentItem.serviceType
        holder.date.text=currentItem.date
        holder.time.text=currentItem.time
        var doctorNameSelected:String?=null
        for ((k,v) in doctorList){
            if (v==currentItem.doctorID){
                doctorNameSelected=k
            }
        }
        holder.doctorName.text=doctorNameSelected
    }
}