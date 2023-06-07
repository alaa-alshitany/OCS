package com.example.ocs.Doctor

data class TreatmentData(
    val treatmentID :String?=null,
    val patientID:String?=null,
    val doctorID:String?=null,
    val patientName:String?=null,
    var IC50: String?=null,
    val drugName:String?=null,
    val classification:String?=null
)
