package com.example.ocs.Admin.Doctors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R

class DoctorAdapter(private var dataList: ArrayList<DoctorData>, private val listener: OnCardListener): RecyclerView.Adapter<DoctorAdapter.ViewHolderClass>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doctor_card, parent, false)
        return ViewHolderClass(itemView)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return dataList.size
    }
    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.doctorName.text ="DR/ ${currentItem.firstName} ${currentItem.lastName}"
        holder.itemView.setOnClickListener {
            listener.onClick(currentItem)
        }
    }
    fun setFilteredList(list:ArrayList<DoctorData>){
        this.dataList=list
        notifyDataSetChanged()
    }

    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView) {
        val doctorName: TextView = itemView.findViewById(R.id.doctorName)
    }
}