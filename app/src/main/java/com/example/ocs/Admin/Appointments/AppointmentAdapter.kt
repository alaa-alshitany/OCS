package com.example.ocs.Admin.Appointments

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.ocs.Patient.booking.AppointmentData
import com.example.ocs.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class AppointmentAdapter (var context: Context, private var dataList: ArrayList<AppointmentData>):
    RecyclerView.Adapter<AppointmentAdapter.ViewHolderClass>() {
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    lateinit var  dialog:Dialog
    private lateinit var dateEntered:LocalDate
    private var currentDate: LocalDate=LocalDate.now()
    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView){
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val patientPhone:TextView=itemView.findViewById(R.id.patientPhone)
        val service:TextView=itemView.findViewById(R.id.patientService)
        val acceptBtn:ImageButton=itemView.findViewById(R.id.accept)
        val cancelBtn:ImageButton=itemView.findViewById(R.id.cancel)

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

        holder.acceptBtn.setOnClickListener {
        dialog=Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.accept_request_dialog)
        dialog.show()
        val displayMetrics= DisplayMetrics()
        val layoutParam=WindowManager.LayoutParams()
        layoutParam.copyFrom(dialog.window?.attributes)
        layoutParam.width=(700)
        layoutParam.height=(1100)
        dialog.window?.attributes=layoutParam
        var fullName:EditText= dialog.findViewById(R.id.fullName_txt)
        fullName.setText(currentItem.patientName)
        var phone:EditText= dialog.findViewById(R.id.phone_txt)
        phone.setText(currentItem.phoneNumber)
        var date:EditText=dialog.findViewById(R.id.appointment_date)
        date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(context,R.style.datepicker,
                DatePickerDialog.OnDateSetListener
                { view, year, monthOfYear, dayOfMonth ->
                    date.setText("$year" +"-"+ (monthOfYear + 1)+"-"+dayOfMonth  )
                    dateEntered= LocalDate.of(year,month,dayOfMonth)
                }, year, month, day
            )
            datePickerDialog.show()
        }

        var time:EditText=dialog.findViewById(R.id.appointment_time)
        time.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)
            val timeDialog=TimePickerDialog(context,R.style.datepicker,TimePickerDialog.OnTimeSetListener{view, hourOfDay, minute ->
                val formattedTime: String = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) { "${hourOfDay + 12}:0${minute} AM" }
                        else { "${hourOfDay + 12}:${minute} AM" }
                    }
                    hourOfDay > 12 -> {
                        if (minute < 10) { "${hourOfDay - 12}:0${minute} PM" }
                        else { "${hourOfDay - 12}:${minute} PM" }
                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) { "${hourOfDay}:0${minute} PM" }
                        else { "${hourOfDay}:${minute} PM" }
                    }
                    else -> {
                        if (minute < 10) { "${hourOfDay}:${minute} AM" }
                        else { "${hourOfDay}:${minute} AM" }
                    }
                }
                time.setText(formattedTime)
            },hour,minute,false
            )
            timeDialog.show()
        }
        var cancelBtn:Button=dialog.findViewById(R.id.cancelBtn)
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        var sendSMS:Button=dialog.findViewById(R.id.sendSMSBtn)
        sendSMS.setOnClickListener {
        if (dateEntered.isBefore(currentDate)) {
            database.child("Appointments").child(currentItem.appointmentID.toString()).child("status").setValue("Approved")
            database.child("Appointments").child(currentItem.appointmentID.toString()).child("date").setValue(dateEntered)
            database.child("Appointments").child(currentItem.appointmentID.toString()).child("time").setValue(time.text)
            //var doctorID=database.child("Doctors").orderByChild("id")
        }else{
            date.setError(R.string.dateError.toString())
            Toast.makeText(context, R.string.dateError, Toast.LENGTH_LONG).show()
        }
        }

        }

        holder.cancelBtn.setOnClickListener {
            database.child("Appointments").child(currentItem.appointmentID.toString()).removeValue().addOnSuccessListener {
                Toast.makeText(holder.itemView.context,"deleted",Toast.LENGTH_LONG).show()
                dataList.clear()
            }
        }
    }

}