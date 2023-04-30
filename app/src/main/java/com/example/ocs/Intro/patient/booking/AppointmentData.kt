package com.example.ocs.Intro.patient.booking

data class AppointmentData(
    val appointmentID :String?=null,
    val date:String?=null,
    val time:String?=null,
    val patientID:String?=null,
    val serviceType:String?=null,
    val doctorID:String?=null,
    val patientName:String?=null,
    var status: String?="unApproved",
    val phoneNumber:String?=null
)
