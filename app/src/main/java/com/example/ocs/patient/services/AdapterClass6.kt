package com.example.ocs.patient.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R

class AdapterClass6 (private val dataList: ArrayList<DataClass6>): RecyclerView.Adapter<AdapterClass6.ViewHolderClass>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout6, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvTitle.text = currentItem.dataservice
        holder.rvdetail.text = currentItem.dataprice
    }

    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvTitle: TextView = itemView.findViewById(R.id.t_title)
        val rvdetail: TextView = itemView.findViewById(R.id.d_title)


    }
}