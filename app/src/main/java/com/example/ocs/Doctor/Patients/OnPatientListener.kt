package com.example.ocs.Doctor

import com.example.ocs.Patient.PatientData

interface OnPatientListener {
    fun onClick(c: PatientData?)
}