package com.example.ocs.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R

class DoctorAdapter(private val dataList: ArrayList<DoctorData>): RecyclerView.Adapter<DoctorAdapter.ViewHolderClass>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doctor_card, parent, false)
        return ViewHolderClass(itemView)
    }
    override fun getItemCount(): Int {
        return dataList.size
    }
    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.doctorName.text ="DR/ "+currentItem.firstName+" "+currentItem.lastName
    }
    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView) {
        val doctorName: TextView = itemView.findViewById(R.id.doctorName)
    }
}