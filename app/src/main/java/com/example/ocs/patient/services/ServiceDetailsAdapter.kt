package com.example.ocs.patient.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R

class ServiceDetailsAdapter (private val dataList: ArrayList<ServiceData>): RecyclerView.Adapter<ServiceDetailsAdapter.ViewHolderClass>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.service_details_item, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.serviceName.setText(currentItem.serviceName)
        holder.servicePrice.setText(currentItem.price)
    }

    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.serviceName)
        val servicePrice: TextView = itemView.findViewById(R.id.servicePrice)
    }
}