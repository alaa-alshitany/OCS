package com.example.ocs.Patient.booking

data class AppointmentData(
    val appointmentID :String?=null,
    val date:String?="",
    val time:String?="",
    val patientID:String?=null,
    val serviceType:String?=null,
    val doctorID:String?="",
    val patientName:String?=null,
    var status: String?="unApproved",
    val phoneNumber:String?=null
)
