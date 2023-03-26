/*package com.example.ocs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppointmentAdapter : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>()
{
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var itemTitle:TextView
        var dateView:TextView
        var TimeView:TextView

        init {
            itemTitle =itemView.findViewById(R.id.nameView)
            dateView =itemView.findViewById(R.id.dateView)
            TimeView =itemview.findViewById(R.id.timeView)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val v =LayoutInflater.from(parent.context).inflate(R.layout.list_appointments,parent,false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.itemTitle .text= titles[position]
        holder.dateView .text= titles[position]
        holder.TimeView .text =titles[position]


    }
}

 */