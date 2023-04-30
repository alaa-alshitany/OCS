package com.example.ocs.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R

class AdapterClass4 (private val dataList: ArrayList<DataClass_approv>): RecyclerView.Adapter<AdapterClass4.ViewHolderClass>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout4, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvTitle.text = currentItem.dataTitle
        holder.rvphone.text = currentItem.data_phone
        holder.rvserve.text = currentItem.data_serve
        holder.rvdate.text = currentItem.data_date
        holder.rvtime.text = currentItem.data_time

    }

    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvTitle: TextView = itemView.findViewById(R.id.t_title)
        val rvphone: TextView = itemView.findViewById(R.id.d_phone)
        val rvserve: TextView = itemView.findViewById(R.id.d_serve)
        val rvdate: TextView = itemView.findViewById(R.id.d_date)
        val rvtime: TextView = itemView.findViewById(R.id.d_time)
    }
}