package com.example.ocs.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R

class AdapterClass_req(private val dataList: ArrayList<DataClass_req>): RecyclerView.Adapter<AdapterClass_req.ViewHolderClass>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout5, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvTitle.text = currentItem.dataTitle
        holder.rvphone.text = currentItem.phonedetail
        holder.rvserve.text = currentItem.servedetail
        holder.rvImage.setImageResource(currentItem.ImageCheck)
        holder.rvImage2.setImageResource(currentItem.Imagecancel)
    }

    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvTitle: TextView = itemView.findViewById(R.id.t_title)
        val rvphone: TextView = itemView.findViewById(R.id.t_phone)
        val rvserve: TextView = itemView.findViewById(R.id.t_serve)
        val rvImage: ImageView = itemView.findViewById(R.id.ckeck)
        val rvImage2: ImageView = itemView.findViewById(R.id.clear)


    }
}