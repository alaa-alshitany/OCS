package com.example.ocs.patient.services

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.R


//adapter-> creates new instance of view items and then feed them to the recycler view
class serviceAdapter(val context:Context, val serviceList: List<serviceModel>, val listener: OnCardItemClickListener) : RecyclerView.Adapter<serviceAdapter.MyViewHolder>() {

    //create new view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // inflates the card_view_design view that is used to hold list item
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.service_item, parent, false)
        return MyViewHolder(view)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return serviceList.size
    }

    //binds the list items to view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cardViewModel = serviceList[position]

        // sets the image to the imageview from our itemHolder class
        holder.serviceImage.setImageResource(cardViewModel.serviceImage)

        // sets the text to the textview from our itemHolder class
        holder.serviceName.setText(cardViewModel.serviceName)

        //listener
        holder.itemView.setOnClickListener {
            listener.onClick(cardViewModel)
        }
        
}
    // Holds the views for adding it to image and text
class MyViewHolder (ItemView: View) : RecyclerView.ViewHolder(ItemView){
    val serviceName:TextView=itemView.findViewById(R.id.serviceNam)
    val serviceImage:ImageView=itemView.findViewById(R.id.serviceImg)

}
}
