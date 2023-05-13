package com.example.ocs.Admin.Appointments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R
import com.example.ocs.Patient.booking.AppointmentData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AppointmentAdapter (private var dataList: ArrayList<AppointmentData>):
    RecyclerView.Adapter<AppointmentAdapter.ViewHolderClass>() {
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView){
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val patientPhone:TextView=itemView.findViewById(R.id.patientPhone)
        val service:TextView=itemView.findViewById(R.id.patientService)
        val acceptBtn:ImageButton=itemView.findViewById(R.id.accept)
        val cancelBtn:ImageButton=itemView.findViewById(R.id.cancel)

    }
        private fun acceptRequest(){

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.appointment_card, parent, false)
        return ViewHolderClass(itemView)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: AppointmentAdapter.ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.patientName.text =currentItem.patientName
        holder.patientPhone.text=currentItem.phoneNumber
        holder.service.text=currentItem.serviceType
        holder.acceptBtn.setOnClickListener { acceptRequest() }
        holder.cancelBtn.setOnClickListener {
            database.child("Appointments").child(currentItem.appointmentID.toString()).removeValue().addOnSuccessListener {
                Toast.makeText(holder.itemView.context,"deleted",Toast.LENGTH_LONG).show()
                dataList.clear()
            }
        }
    }
}