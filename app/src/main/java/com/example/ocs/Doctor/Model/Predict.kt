package com.example.ocs.Doctor.Model

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ocs.Doctor.Appointments.Appointments
import com.example.ocs.Doctor.DoctorProfile.Profile
import com.example.ocs.Doctor.Patients
import com.example.ocs.Doctor.TreatmentData
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.Login_Register.login.Prefrences
import com.example.ocs.Patient.PatientData
import com.example.ocs.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.opencsv.RFC4180ParserBuilder
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer

class Predict: AppCompatActivity() {
    private val appointmentRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var treatmentID:String
    private lateinit var patientSpinner:Spinner
    private lateinit var drugSpinner:Spinner
    val database = FirebaseDatabase.getInstance().reference
    var patientRef = FirebaseStorage.getInstance().getReference("Test_Data/Patient_Data/")
    var hklRef = FirebaseStorage.getInstance().getReference("Test_Data/HKL/")
    var drugRef = FirebaseStorage.getInstance().getReference("Test_Data/Drug_Data/")
    private lateinit var adapter:ArrayAdapter<String>
    private lateinit var predictBtn: Button
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var pref: Prefrences
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var context: Context
    private lateinit var navHeader : View
    private lateinit var userName: TextView
    private lateinit var  dialog: Dialog
    private lateinit var selectedPatientName:TextView
    private lateinit var selectedDrugName:TextView
    private lateinit var predictiedIC50:TextView
    private lateinit var classification:TextView
    private lateinit var preditingProgress:ProgressBar
    private lateinit var classificationProgress:ProgressBar
    private lateinit var saveRecord:Button
    private lateinit var tryDrug:Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.predict)
        supportActionBar!!.elevation=0F
        init()
        //nav_bar
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nau_profile1 -> doctorProfile()
                R.id.nau_booking1 -> appointment()
                R.id.nau_patient -> patients()
                R.id.nau_model -> model()
                R.id.nau_logout1 -> logout()
            }

            true
        }
        predictBtn.setOnClickListener {
            predictIC50_classification()


                        /*
                        FirebaseModelDownloader.getInstance().getModel("Classification", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions).addOnSuccessListener {
                            modelFile = it?.file
                            if (modelFile != null) {
                                Toast.makeText(
                                    this,
                                    "Classification Model downloaded successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                interpreter = Interpreter(modelFile!!)
                                outputShape = interpreter.getOutputTensor(0).shape()
                                outputDataType = interpreter.getOutputTensor(0).dataType()
                                outputBuffer =
                                    TensorBuffer.createFixedSize(outputShape, outputDataType)
                                interpreter.runForMultipleInputsOutputs(inputs, outputs)
                                output = outputBuffer.floatArray[0]
                                val roundedOutput = Math.round(output * 1000.0) / 1000.0
                                val outputBinary = if (roundedOutput >= 0.5) 1 else 0
                                progressBar.visibility = View.GONE
                                if (outputBinary == 1) {
                                    Toast.makeText(
                                        this,
                                        "Drug ${drugSpinner.selectedItem.toString()} is Resistance for ${patientSpinner.selectedItem.toString()} with percentage:  ${roundedOutput * 100} %",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Drug ${drugSpinner.selectedItem.toString()} is Sensitive for ${patientSpinner.selectedItem.toString()} with percentage:  ${roundedOutput * 100} %",
                                        Toast.LENGTH_LONG
                                    ).show()

                                }

                            }
                        }
*/

        }
    }
private fun predictIC50_classification(){
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.model_dialog_layout)
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()
    val displayMetrics= DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val layoutParam= WindowManager.LayoutParams()
    layoutParam.copyFrom(dialog.window?.attributes)
    layoutParam.width=(displayMetrics.widthPixels *0.9F).toInt()
    layoutParam.height=(displayMetrics.heightPixels *0.9F).toInt()
    dialog.window?.attributes=layoutParam
    selectedPatientName=dialog.findViewById(R.id.patientName)
    selectedPatientName.setText(patientSpinner.selectedItem.toString())
    selectedDrugName=dialog.findViewById(R.id.drugName)
    selectedDrugName.setText(drugSpinner.selectedItem.toString())
    preditingProgress=dialog.findViewById(R.id.predictingProgressBar)
    preditingProgress.progress=0
    classificationProgress.progress=0
    predictiedIC50=dialog.findViewById(R.id.predictiedIC50)
    saveRecord=dialog.findViewById(R.id.saveRecord_btn)
    tryDrug=dialog.findViewById(R.id.tryDrug_btn)
    patientRef = patientRef.child(patientSpinner.selectedItem.toString())
    when (drugSpinner.selectedItem.toString()) {
        "(5Z)-7-Oxozeaenol" -> {
            drugRef = drugRef.child("9863776.csv")
            hklRef = hklRef.child("9863776.hkl")
        }
        "5-Fluorouracil" -> {
            drugRef = drugRef.child("3385.csv")
            hklRef = hklRef.child("3385.hkl")
        }
        "A-443654" -> {
            drugRef = drugRef.child("10172943.csv")
            hklRef = hklRef.child("10172943.hkl")
        }
        "A-770041" -> {
            drugRef = drugRef.child("9549184.csv")
            hklRef = hklRef.child("9549184.hkl")
        }
        "AICA Ribonucleotide" -> {
            drugRef = drugRef.child("65110.csv")
            hklRef = hklRef.child("65110.hkl")
        }
        "AKT inhibitor VIII" -> {
            drugRef = drugRef.child("10196499.csv")
            hklRef = hklRef.child("10196499.hkl")
        }
        "AR-42" -> {
            drugRef = drugRef.child("6918848.csv")
            hklRef = hklRef.child("6918848.hkl")
        }
        "AS601245" -> {
            drugRef = drugRef.child("10109823.csv")
            hklRef = hklRef.child("10109823.hkl")
        }
        "AS605240" -> {
            drugRef = drugRef.child("5289247.csv")
            hklRef = hklRef.child("5289247.hkl")
        }
        "AT-7519" -> {
            drugRef = drugRef.child("11338033.csv")
            hklRef = hklRef.child("11338033.hkl")
        }
        "AZ628" -> {
            drugRef = drugRef.child("11676786.csv")
            hklRef = hklRef.child("11676786.hkl")
        }
        "AZD6482" -> {
            drugRef = drugRef.child("44137675.csv")
            hklRef = hklRef.child("44137675.hkl")
        }
        "AZD7762" -> {
            drugRef = drugRef.child("11152667.csv")
            hklRef = hklRef.child("11152667.hkl")
        }
        "AZD8055" -> {
            drugRef = drugRef.child("25262965.csv")
            hklRef = hklRef.child("25262965.hkl")
        }
        "Afatinib" -> {
            drugRef = drugRef.child("10184653.csv")
            hklRef = hklRef.child("10184653.hkl")
        }
        "Alectinib" -> {
            drugRef = drugRef.child("49806720.csv")
            hklRef = hklRef.child("49806720.hkl")
        }
        "Amuvatinib" -> {
            drugRef = drugRef.child("11282283.csv")
            hklRef = hklRef.child("11282283.hkl")
        }
        "Avagacestat" -> {
            drugRef = drugRef.child("46883536.csv")
            hklRef = hklRef.child("46883536.hkl")
        }
        "Axitinib" -> {
            drugRef = drugRef.child("6450551.csv")
            hklRef = hklRef.child("6450551.hkl")
        }
        "BAY-61-3606" -> {
            drugRef = drugRef.child("10200390.csv")
            hklRef = hklRef.child("10200390.hkl")
        }
        "BI-2536" -> {
            drugRef = drugRef.child("11364421.csv")
            hklRef = hklRef.child("11364421.hkl")
        }
        "BIX02189" -> {
            drugRef = drugRef.child("46931012.csv")
            hklRef = hklRef.child("46931012.hkl")
        }
        ////////////////////////////////////////////////////////
        "BMS-345541" -> {
            drugRef = drugRef.child("9813758.csv")
            hklRef = hklRef.child("9813758.hkl")
        }
        "BMS-509744" -> {
            drugRef = drugRef.child("20635522.csv")
            hklRef = hklRef.child("20635522.hkl")
        }
        "BMS-536924" -> {
            drugRef = drugRef.child("10390396.csv")
            hklRef = hklRef.child("10390396.hkl")
        }
        "BMS-754807" -> {
            drugRef = drugRef.child("24785538.csv")
            hklRef = hklRef.child("24785538.hkl")
        }
        "BX-912" -> {
            drugRef = drugRef.child("11754511.csv")
            hklRef = hklRef.child("11754511.hkl")
        }
        "BX795" -> {
            drugRef = drugRef.child("10077147.csv")
            hklRef = hklRef.child("10077147.hkl")
        }
        "Belinostat" -> {
            drugRef = drugRef.child("6918638.csv")
            hklRef = hklRef.child("6918638.hkl")
        }
        "Bexarotene" -> {
            drugRef = drugRef.child("82146.csv")
            hklRef = hklRef.child("82146.hkl")
        }
        "Bicalutamide" -> {
            drugRef = drugRef.child("2375.csv")
            hklRef = hklRef.child("2375.hkl")
        }
        "Bleomycin" -> {
            drugRef = drugRef.child("5460769.csv")
            hklRef = hklRef.child("5460769.hkl")
        }
        "Bortezomib" -> {
            drugRef = drugRef.child("387447.csv")
            hklRef = hklRef.child("387447.hkl")
        }
        "Bosutinib" -> {
            drugRef = drugRef.child("5328940.csv")
            hklRef = hklRef.child("5328940.hkl")
        }
        "Bryostatin 1" -> {
            drugRef = drugRef.child("5280757.csv")
            hklRef = hklRef.child("5280757.hkl")
        }
        "CAY10603" -> {
            drugRef = drugRef.child("24951314.csv")
            hklRef = hklRef.child("24951314.hkl")
        }
        "CCT-018159" -> {
            drugRef = drugRef.child("5327091.csv")
            hklRef = hklRef.child("5327091.hkl")
        }
        "CCT007093" -> {
            drugRef = drugRef.child("2314623.csv")
            hklRef = hklRef.child("2314623.hkl")
        }
        "CGP-082996" -> {
            drugRef = drugRef.child("24825971.csv")
            hklRef = hklRef.child("24825971.hkl")
        }
        "CGP-60474" -> {
            drugRef = drugRef.child("644215.csv")
            hklRef = hklRef.child("644215.hkl")
        }
        "CHIR-99021" -> {
            drugRef = drugRef.child("9956119.csv")
            hklRef = hklRef.child("9956119.hkl")
        }
        "CI-1040" -> {
            drugRef = drugRef.child("6918454.csv")
            hklRef = hklRef.child("6918454.hkl")
        }
        "CMK" -> {
            drugRef = drugRef.child("16663089.csv")
            hklRef = hklRef.child("16663089.hkl")
        }
        "CP466722" -> {
            drugRef = drugRef.child("44551660.csv")
            hklRef = hklRef.child("44551660.hkl")
        }
        "CP724714" -> {
            drugRef = drugRef.child("9874913.csv")
            hklRef = hklRef.child("9874913.hkl")
        }
        "CUDC-101" -> {
            drugRef = drugRef.child("24756910.csv")
            hklRef = hklRef.child("24756910.hkl")
        }
        "CX-5461" -> {
            drugRef = drugRef.child("25257557.csv")
            hklRef = hklRef.child("25257557.hkl")
        }
        "Cabozantinib" -> {
            drugRef = drugRef.child("25102847.csv")
            hklRef = hklRef.child("25102847.hkl")
        }
        "Cetuximab" -> {
            drugRef = drugRef.child("85668777.csv")
            hklRef = hklRef.child("85668777.hkl")
        }
        "Cisplatin" -> {
            drugRef = drugRef.child("84691.csv")
            hklRef = hklRef.child("84691.hkl")
        }
        "Crizotinib" -> {
            drugRef = drugRef.child("11626560.csv")
            hklRef = hklRef.child("11626560.hkl")
        }
        "Cytarabine" -> {
            drugRef = drugRef.child("6253.csv")
            hklRef = hklRef.child("6253.hkl")
        }
        "DMOG" -> {
            drugRef = drugRef.child("560326.csv")
            hklRef = hklRef.child("560326.hkl")
        }
        "Dabrafenib" -> {
            drugRef = drugRef.child("44462760.csv")
            hklRef = hklRef.child("44462760.hkl")
        }
        "Dacinostat" -> {
            drugRef = drugRef.child("6445533.csv")
            hklRef = hklRef.child("6445533.hkl")
        }
        "Dactolisib" -> {
            drugRef = drugRef.child("11977753.csv")
            hklRef = hklRef.child("11977753.hkl")
        }
        "Daporinad" -> {
            drugRef = drugRef.child("6914657.csv")
            hklRef = hklRef.child("6914657.hkl")
        }
        "Dasatinib" -> {
            drugRef = drugRef.child("3062316.csv")
            hklRef = hklRef.child("3062316.hkl")
        }
        "Docetaxel" -> {
            drugRef = drugRef.child("148124.csv")
            hklRef = hklRef.child("148124.hkl")
        }
        "Doramapimod" -> {
            drugRef = drugRef.child("156422.csv")
            hklRef = hklRef.child("156422.hkl")
        }
        "Doxorubicin" -> {
            drugRef = drugRef.child("31703.csv")
            hklRef = hklRef.child("31703.hkl")
        }
        "EHT-1864" -> {
            drugRef = drugRef.child("9938202.csv")
            hklRef = hklRef.child("9938202.hkl")
        }
        "Elesclomol" -> {
            drugRef = drugRef.child("300471.csv")
            hklRef = hklRef.child("300471.hkl")
        }
        "Embelin" -> {
            drugRef = drugRef.child("3218.csv")
            hklRef = hklRef.child("3218.hkl")
        }
        "Entinostat" -> {
            drugRef = drugRef.child("4261.csv")
            hklRef = hklRef.child("4261.hkl")
        }
        "Enzastaurin" -> {
            drugRef = drugRef.child("176167.csv")
            hklRef = hklRef.child("176167.hkl")
        }
        "Epothilone B" -> {
            drugRef = drugRef.child("448013.csv")
            hklRef = hklRef.child("448013.hkl")
        }
        "Erlotinib" -> {
            drugRef = drugRef.child("176870.csv")
            hklRef = hklRef.child("176870.hkl")
        }
        "Etoposide" -> {
            drugRef = drugRef.child("36462.csv")
            hklRef = hklRef.child("36462.hkl")
        }
        "FH535" -> {
            drugRef = drugRef.child("3463933.csv")
            hklRef = hklRef.child("3463933.hkl")
        }
        "FR-180204" -> {
            drugRef = drugRef.child("11493598.csv")
            hklRef = hklRef.child("11493598.hkl")
        }
        "FTI-277" -> {
            drugRef = drugRef.child("3005532.csv")
            hklRef = hklRef.child("3005532.hkl")
        }
        "Fedratinib" -> {
            drugRef = drugRef.child("16722836.csv")
            hklRef = hklRef.child("16722836.hkl")
        }
        "Foretinib" -> {
            drugRef = drugRef.child("42642645.csv")
            hklRef = hklRef.child("42642645.hkl")
        }
        "GNF-2" -> {
            drugRef = drugRef.child("5311510.csv")
            hklRef = hklRef.child("5311510.hkl")
        }
        "GSK1070916" -> {
            drugRef = drugRef.child("46885626.csv")
            hklRef = hklRef.child("46885626.hkl")
        }
        "GSK1904529A" -> {
            drugRef = drugRef.child("25124816.csv")
            hklRef = hklRef.child("25124816.hkl")
        }
        "GSK269962A" -> {
            drugRef = drugRef.child("16095342.csv")
            hklRef = hklRef.child("16095342.hkl")
        }
        "GSK319347A" -> {
            drugRef = drugRef.child("11626927.csv")
            hklRef = hklRef.child("11626927.hkl")
        }
        "GSK429286A" -> {
            drugRef = drugRef.child("11373846.csv")
            hklRef = hklRef.child("11373846.hkl")
        }
        "GSK650394" -> {
            drugRef = drugRef.child("25022668.csv")
            hklRef = hklRef.child("25022668.hkl")
        }
        "GSK690693" -> {
            drugRef = drugRef.child("16725726.csv")
            hklRef = hklRef.child("16725726.hkl")
        }
        "GW-2580" -> {
            drugRef = drugRef.child("11617559.csv")
            hklRef = hklRef.child("11617559.hkl")
        }
        "GW441756" -> {
            drugRef = drugRef.child("9943465.csv")
            hklRef = hklRef.child("9943465.hkl")
        }
        "GW843682X" -> {
            drugRef = drugRef.child("9826308.csv")
            hklRef = hklRef.child("9826308.hkl")
        }
        "Gefitinib" -> {
            drugRef = drugRef.child("123631.csv")
            hklRef = hklRef.child("123631.hkl")
        }
        "Gemcitabine" -> {
            drugRef = drugRef.child("60750.csv")
            hklRef = hklRef.child("60750.hkl")
        }
        "HG6-64-1" -> {
            drugRef = drugRef.child("53302361.csv")
            hklRef = hklRef.child("53302361.hkl")
        }
        "I-BET-762" -> {
            drugRef = drugRef.child("46943432.csv")
            hklRef = hklRef.child("46943432.hkl")
        }
        "IOX2" -> {
            drugRef = drugRef.child("54685215.csv")
            hklRef = hklRef.child("54685215.hkl")
        }
        "IPA-3" -> {
            drugRef = drugRef.child("521106.csv")
            hklRef = hklRef.child("521106.hkl")
        }
        "Idelalisib" -> {
            drugRef = drugRef.child("11625818.csv")
            hklRef = hklRef.child("11625818.hkl")
        }
        "Imatinib" -> {
            drugRef = drugRef.child("5291.csv")
            hklRef = hklRef.child("5291.hkl")
        }
        "Ispinesib Mesylate" -> {
            drugRef = drugRef.child("6450816.csv")
            hklRef = hklRef.child("6450816.hkl")
        }
        "JNK Inhibitor VIII" -> {
            drugRef = drugRef.child("11624601.csv")
            hklRef = hklRef.child("11624601.hkl")
        }
        "JNK-9L" -> {
            drugRef = drugRef.child("25222038.csv")
            hklRef = hklRef.child("25222038.hkl")
        }
        "JQ1" -> {
            drugRef = drugRef.child("46907787.csv")
            hklRef = hklRef.child("46907787.hkl")
        }
        "JW-7-52-1" -> {
            drugRef = drugRef.child("49836027.csv")
            hklRef = hklRef.child("49836027.hkl")
        }
        "KIN001-244" -> {
            drugRef = drugRef.child("56965967.csv")
            hklRef = hklRef.child("56965967.hkl")
        }
        "KIN001-260" -> {
            drugRef = drugRef.child("10451420.csv")
            hklRef = hklRef.child("10451420.hkl")
        }
        "KIN001-266" -> {
            drugRef = drugRef.child("44143370.csv")
            hklRef = hklRef.child("44143370.hkl")
        }
        "KIN001-270" -> {
            drugRef = drugRef.child("66577006.csv")
            hklRef = hklRef.child("66577006.hkl")
        }
        "KU-55933" -> {
            drugRef = drugRef.child("5278396.csv")
            hklRef = hklRef.child("5278396.hkl")
        }
        "LFM-A13" -> {
            drugRef = drugRef.child("54676905.csv")
            hklRef = hklRef.child("54676905.hkl")
        }
        "Lapatinib" -> {
            drugRef = drugRef.child("208908.csv")
            hklRef = hklRef.child("208908.hkl")
        }
        "Lenalidomide" -> {
            drugRef = drugRef.child("216326.csv")
            hklRef = hklRef.child("216326.hkl")
        }
        "Lestaurtinib" -> {
            drugRef = drugRef.child("126565.csv")
            hklRef = hklRef.child("126565.hkl")
        }
        "Linifanib" -> {
            drugRef = drugRef.child("11485656.csv")
            hklRef = hklRef.child("11485656.hkl")
        }
        "Linsitinib" -> {
            drugRef = drugRef.child("11640390.csv")
            hklRef = hklRef.child("11640390.hkl")
        }
        "Luminespib" -> {
            drugRef = drugRef.child("10096043.csv")
            hklRef = hklRef.child("10096043.hkl")
        }
        "MG-132" -> {
            drugRef = drugRef.child("462382.csv")
            hklRef = hklRef.child("462382.hkl")
        }
        "MK-2206" -> {
            drugRef = drugRef.child("46930998.csv")
            hklRef = hklRef.child("46930998.hkl")
        }
        "MPS-1-IN-1" -> {
            drugRef = drugRef.child("25195352.csv")
            hklRef = hklRef.child("25195352.hkl")
        }
        "Masitinib" -> {
            drugRef = drugRef.child("10074640.csv")
            hklRef = hklRef.child("10074640.hkl")
        }
        "Methotrexate" -> {
            drugRef = drugRef.child("126941.csv")
            hklRef = hklRef.child("126941.hkl")
        }
        "Mitomycin-C" -> {
            drugRef = drugRef.child("5746.csv")
            hklRef = hklRef.child("5746.hkl")
        }
        "Motesanib" -> {
            drugRef = drugRef.child("11667893.csv")
            hklRef = hklRef.child("11667893.hkl")
        }
        "NG-25" -> {
            drugRef = drugRef.child("53340664.csv")
            hklRef = hklRef.child("53340664.hkl")
        }
        "NSC-207895" -> {
            drugRef = drugRef.child("42640.csv")
            hklRef = hklRef.child("42640.hkl")
        }
        "NSC-87877" -> {
            drugRef = drugRef.child("5459322.csv")
            hklRef = hklRef.child("5459322.hkl")
        }
        "NU7441" -> {
            drugRef = drugRef.child("11327430.csv")
            hklRef = hklRef.child("11327430.hkl")
        }
        "NVP-BHG712" -> {
            drugRef = drugRef.child("16747388.csv")
            hklRef = hklRef.child("16747388.hkl")
        }
        "NVP-TAE684" -> {
            drugRef = drugRef.child("16038120.csv")
            hklRef = hklRef.child("16038120.hkl")
        }
        "Navitoclax" -> {
            drugRef = drugRef.child("24978538.csv")
            hklRef = hklRef.child("24978538.hkl")
        }
        "Nilotinib" -> {
            drugRef = drugRef.child("644241.csv")
            hklRef = hklRef.child("644241.hkl")
        }
        "Nutlin-3a (-)" -> {
            drugRef = drugRef.child("11433190.csv")
            hklRef = hklRef.child("11433190.hkl")
        }
        "OSI-027" -> {
            drugRef = drugRef.child("44224160.csv")
            hklRef = hklRef.child("44224160.hkl")
        }
        "OSI-930" -> {
            drugRef = drugRef.child("9868037.csv")
            hklRef = hklRef.child("9868037.hkl")
        }
        "OSU-03012" -> {
            drugRef = drugRef.child("10027278.csv")
            hklRef = hklRef.child("10027278.hkl")
        }
        "Obatoclax Mesylate" -> {
            drugRef = drugRef.child("11404337.csv")
            hklRef = hklRef.child("11404337.hkl")
        }
        "Olaparib" -> {
            drugRef = drugRef.child("23725625.csv")
            hklRef = hklRef.child("23725625.hkl")
        }
        "Omipalisib" -> {
            drugRef = drugRef.child("25167777.csv")
            hklRef = hklRef.child("25167777.hkl")
        }
        "PAC-1" -> {
            drugRef = drugRef.child("6753378.csv")
            hklRef = hklRef.child("6753378.hkl")
        }
        "PD0325901" -> {
            drugRef = drugRef.child("9826528.csv")
            hklRef = hklRef.child("9826528.hkl")
        }
        "PD173074" -> {
            drugRef = drugRef.child("1401.csv")
            hklRef = hklRef.child("1401.hkl")
        }
        "PF-4708671" -> {
            drugRef = drugRef.child("51371303.csv")
            hklRef = hklRef.child("51371303.hkl")
        }
        "PF-562271" -> {
            drugRef = drugRef.child("11713159.csv")
            hklRef = hklRef.child("11713159.hkl")
        }
        "PFI-1" -> {
            drugRef = drugRef.child("71271629.csv")
            hklRef = hklRef.child("71271629.hkl")
        }
        "PFI-3" -> {
            drugRef = drugRef.child("78243717.csv")
            hklRef = hklRef.child("78243717.hkl")
        }
        "PHA-665752" -> {
            drugRef = drugRef.child("10461815.csv")
            hklRef = hklRef.child("10461815.hkl")
        }
        "PHA-793887" -> {
            drugRef = drugRef.child("46191454.csv")
            hklRef = hklRef.child("46191454.hkl")
        }
        "PI-103" -> {
            drugRef = drugRef.child("9884685.csv")
            hklRef = hklRef.child("9884685.hkl")
        }
        "PIK-93" -> {
            drugRef = drugRef.child("6852167.csv")
            hklRef = hklRef.child("6852167.hkl")
        }
        "PLX-4720" -> {
            drugRef = drugRef.child("24180719.csv")
            hklRef = hklRef.child("24180719.hkl")
        }
        "Paclitaxel" -> {
            drugRef = drugRef.child("36314.csv")
            hklRef = hklRef.child("36314.hkl")
        }
        "Palbociclib" -> {
            drugRef = drugRef.child("5330286.csv")
            hklRef = hklRef.child("5330286.hkl")
        }
        "Parthenolide" -> {
            drugRef = drugRef.child("7251185.csv")
            hklRef = hklRef.child("7251185.hkl")
        }
        "Pazopanib" -> {
            drugRef = drugRef.child("10113978.csv")
            hklRef = hklRef.child("10113978.hkl")
        }
        "Pelitinib" -> {
            drugRef = drugRef.child("6445562.csv")
            hklRef = hklRef.child("6445562.hkl")
        }
        "Pevonedistat" -> {
            drugRef = drugRef.child("16720766.csv")
            hklRef = hklRef.child("16720766.hkl")
        }
        "Phenformin" -> {
            drugRef = drugRef.child("8249.csv")
            hklRef = hklRef.child("8249.hkl")
        }
        "Pictilisib" -> {
            drugRef = drugRef.child("17755052.csv")
            hklRef = hklRef.child("17755052.hkl")
        }
        "Piperlongumine" -> {
            drugRef = drugRef.child("637858.csv")
            hklRef = hklRef.child("637858.hkl")
        }
        "Ponatinib" -> {
            drugRef = drugRef.child("24826799.csv")
            hklRef = hklRef.child("24826799.hkl")
        }
        "Pyrimethamine" -> {
            drugRef = drugRef.child("4993.csv")
            hklRef = hklRef.child("4993.hkl")
        }
        "QS11" -> {
            drugRef = drugRef.child("4263900.csv")
            hklRef = hklRef.child("4263900.hkl")
        }
        "Quizartinib" -> {
            drugRef = drugRef.child("24889392.csv")
            hklRef = hklRef.child("24889392.hkl")
        }
        "RO-3306" -> {
            drugRef = drugRef.child("44450571.csv")
            hklRef = hklRef.child("44450571.hkl")
        }
        "Rapamycin" -> {
            drugRef = drugRef.child("5384616.csv")
            hklRef = hklRef.child("5384616.hkl")
        }
        "Refametinib" -> {
            drugRef = drugRef.child("44182295.csv")
            hklRef = hklRef.child("44182295.hkl")
        }
        "Rucaparib" -> {
            drugRef = drugRef.child("9931953.csv")
            hklRef = hklRef.child("9931953.hkl")
        }
        "Ruxolitinib" -> {
            drugRef = drugRef.child("25126798.csv")
            hklRef = hklRef.child("25126798.hkl")
        }
        "S-Trityl-L-cysteine" -> {
            drugRef = drugRef.child("76044.csv")
            hklRef = hklRef.child("76044.hkl")
        }
        "SB216763" -> {
            drugRef = drugRef.child("176158.csv")
            hklRef = hklRef.child("176158.hkl")
        }
        "SB505124" -> {
            drugRef = drugRef.child("9858940.csv")
            hklRef = hklRef.child("9858940.hkl")
        }
        "SB52334" -> {
            drugRef = drugRef.child("9967941.csv")
            hklRef = hklRef.child("9967941.hkl")
        }
        "SB590885" -> {
            drugRef = drugRef.child("11316960.csv")
            hklRef = hklRef.child("11316960.hkl")
        }
        "SGC0946" -> {
            drugRef = drugRef.child("56962337.csv")
            hklRef = hklRef.child("56962337.hkl")
        }
        "SL0101" -> {
            drugRef = drugRef.child("10459196.csv")
            hklRef = hklRef.child("10459196.hkl")
        }
        "SN-38" -> {
            drugRef = drugRef.child("104842.csv")
            hklRef = hklRef.child("104842.hkl")
        }
        "SNX-2112" -> {
            drugRef = drugRef.child("24772860.csv")
            hklRef = hklRef.child("24772860.hkl")
        }
        "STF-62247" -> {
            drugRef = drugRef.child("704473.csv")
            hklRef = hklRef.child("704473.hkl")
        }
        "Salubrinal" -> {
            drugRef = drugRef.child("5717801.csv")
            hklRef = hklRef.child("5717801.hkl")
        }
        "Saracatinib" -> {
            drugRef = drugRef.child("10302451.csv")
            hklRef = hklRef.child("10302451.hkl")
        }
        "Seliciclib" -> {
            drugRef = drugRef.child("160355.csv")
            hklRef = hklRef.child("160355.hkl")
        }
        "Selisistat" -> {
            drugRef = drugRef.child("5113032.csv")
            hklRef = hklRef.child("5113032.hkl")
        }
        "Selumetinib" -> {
            drugRef = drugRef.child("10127622.csv")
            hklRef = hklRef.child("10127622.hkl")
        }
        "Sepantronium bromide" -> {
            drugRef = drugRef.child("11178236.csv")
            hklRef = hklRef.child("11178236.hkl")
        }
        "Serdemetan" -> {
            drugRef = drugRef.child("11609586.csv")
            hklRef = hklRef.child("11609586.hkl")
        }
        "Shikonin" -> {
            drugRef = drugRef.child("5208.csv")
            hklRef = hklRef.child("5208.hkl")
        }
        "Sorafenib" -> {
            drugRef = drugRef.child("216239.csv")
            hklRef = hklRef.child("216239.hkl")
        }
        "Sunitinib" -> {
            drugRef = drugRef.child("5329102.csv")
            hklRef = hklRef.child("5329102.hkl")
        }
        "T0901317" -> {
            drugRef = drugRef.child("447912.csv")
            hklRef = hklRef.child("447912.hkl")
        }
        "TAK-715" -> {
            drugRef = drugRef.child("9952773.csv")
            hklRef = hklRef.child("9952773.hkl")
        }
        "TGX221" -> {
            drugRef = drugRef.child("9907093.csv")
            hklRef = hklRef.child("9907093.hkl")
        }
        "TPCA-1" -> {
            drugRef = drugRef.child("9903786.csv")
            hklRef = hklRef.child("9903786.hkl")
        }
        "TW 37" -> {
            drugRef = drugRef.child("11455910.csv")
            hklRef = hklRef.child("11455910.hkl")
        }
        "Talazoparib" -> {
            drugRef = drugRef.child("44819241.csv")
            hklRef = hklRef.child("44819241.hkl")
        }
        "Tamoxifen" -> {
            drugRef = drugRef.child("2733526.csv")
            hklRef = hklRef.child("2733526.hkl")
        }
        "Tanespimycin" -> {
            drugRef = drugRef.child("6505803.csv")
            hklRef = hklRef.child("6505803.hkl")
        }
        "Temozolomide" -> {
            drugRef = drugRef.child("5394.csv")
            hklRef = hklRef.child("5394.hkl")
        }
        "Temsirolimus" -> {
            drugRef = drugRef.child("6918289.csv")
            hklRef = hklRef.child("6918289.hkl")
        }
        "Thapsigargin" -> {
            drugRef = drugRef.child("446378.csv")
            hklRef = hklRef.child("446378.hkl")
        }
        "Tipifarnib" -> {
            drugRef = drugRef.child("159324.csv")
            hklRef = hklRef.child("159324.hkl")
        }
        "Tivozanib" -> {
            drugRef = drugRef.child("9911830.csv")
            hklRef = hklRef.child("9911830.hkl")
        }
        "Tozasertib" -> {
            drugRef = drugRef.child("5494449.csv")
            hklRef = hklRef.child("5494449.hkl")
        }
        "Trametinib" -> {
            drugRef = drugRef.child("11707110.csv")
            hklRef = hklRef.child("11707110.hkl")
        }
        "Tretinoin" -> {
            drugRef = drugRef.child("444795.csv")
            hklRef = hklRef.child("444795.hkl")
        }
        "Tubastatin A" -> {
            drugRef = drugRef.child("53394750.csv")
            hklRef = hklRef.child("53394750.hkl")
        }
        "UNC0638" -> {
            drugRef = drugRef.child("46224516.csv")
            hklRef = hklRef.child("46224516.hkl")
        }
        "UNC1215" -> {
            drugRef = drugRef.child("57339144.csv")
            hklRef = hklRef.child("57339144.hkl")
        }
        "VNLG/124" -> {
            drugRef = drugRef.child("24894414.csv")
            hklRef = hklRef.child("24894414.hkl")
        }
        "VX-11e" -> {
            drugRef = drugRef.child("11634725.csv")
            hklRef = hklRef.child("11634725.hkl")
        }
        "VX-702" -> {
            drugRef = drugRef.child("10341154.csv")
            hklRef = hklRef.child("10341154.hkl")
        }
        "Veliparib" -> {
            drugRef = drugRef.child("11960529.csv")
            hklRef = hklRef.child("11960529.hkl")
        }
        "Vinblastine" -> {
            drugRef = drugRef.child("6710780.csv")
            hklRef = hklRef.child("6710780.hkl")
        }
        "Vinorelbine" -> {
            drugRef = drugRef.child("5311497.csv")
            hklRef = hklRef.child("5311497.hkl")
        }
        "Vismodegib" -> {
            drugRef = drugRef.child("24776445.csv")
            hklRef = hklRef.child("24776445.hkl")
        }
        "Vorinostat" -> {
            drugRef = drugRef.child("5311.csv")
            hklRef = hklRef.child("5311.hkl")
        }
        "WH-4-023" -> {
            drugRef = drugRef.child("11844351.csv")
            hklRef = hklRef.child("11844351.hkl")
        }
        "WHI-P97" -> {
            drugRef = drugRef.child("3796.csv")
            hklRef = hklRef.child("3796.hkl")
        }
        "WZ-1-84" -> {
            drugRef = drugRef.child("49821040.csv")
            hklRef = hklRef.child("49821040.hkl")
        }
        "Wee1 Inhibitor" -> {
            drugRef = drugRef.child("10384072.csv")
            hklRef = hklRef.child("10384072.hkl")
        }
        "XAV939" -> {
            drugRef = drugRef.child("2726824.csv")
            hklRef = hklRef.child("2726824.hkl")
        }
        "XMD8-85" -> {
            drugRef = drugRef.child("46844147.csv")
            hklRef = hklRef.child("46844147.hkl")
        }
        "XMD8-92" -> {
            drugRef = drugRef.child("46843772.csv")
            hklRef = hklRef.child("46843772.hkl")
        }
        "Y-39983" -> {
            drugRef = drugRef.child("9810884.csv")
            hklRef = hklRef.child("9810884.hkl")
        }
        "YK-4-279" -> {
            drugRef = drugRef.child("44632017.csv")
            hklRef = hklRef.child("44632017.hkl")
        }
        "YM201636" -> {
            drugRef = drugRef.child("9956222.csv")
            hklRef = hklRef.child("9956222.hkl")
        }
        "Z-LLNle-CHO" -> {
            drugRef = drugRef.child("16760646.csv")
            hklRef = hklRef.child("16760646.hkl")
        }
        "ZM447439" -> {
            drugRef = drugRef.child("9914412.csv")
            hklRef = hklRef.child("9914412.hkl")
        }
        "ZSTK474" -> {
            drugRef = drugRef.child("11647372.csv")
            hklRef = hklRef.child("11647372.hkl")
        }
        "Zibotentan" -> {
            drugRef = drugRef.child("9910224.csv")
            hklRef = hklRef.child("9910224.hkl")
        }

        else -> {
            drugRef = drugRef.child("9914412.csv")
            hklRef = hklRef.child("9914412.hkl")
        }
    }
    val localFile = File.createTempFile("temp", ".hkl")
    hklRef.getFile(localFile).addOnSuccessListener {
        // Read the contents of the file into a byte array
        val bytes = localFile.readBytes()
        val floatArray = FloatArray(75)
        ByteBuffer.wrap(bytes).asFloatBuffer().get(floatArray)
        for (i in floatArray.indices) {
            if (floatArray[i].isNaN()) {
                floatArray[i] = 1f
            }
        }
        val drugFeatTensor =
            TensorBuffer.createFixedSize(intArrayOf(1, 1, 75), DataType.FLOAT32)
        drugFeatTensor.loadArray(floatArray)
        ////////////////////////////////////////////////////////////////////////////////////
        val MutFile = patientRef.child("genomic_mutation.csv")
        val mutInput = extractNumericValuesFromCsvRow(MutFile, intArrayOf(1, 1, 34673, 1))
        val mutInputTensor =
            TensorBuffer.createFixedSize(intArrayOf(1, 1, 34673, 1), DataType.FLOAT32)
        mutInputTensor.loadArray(mutInput)
        //////////////////////////////////////////////////////////////////////////
        val expFile = patientRef.child("genomic_expression.csv")
        val expInput = extractNumericValuesFromCsvRow(expFile, intArrayOf(1, 697))
        val expInputTensor =
            TensorBuffer.createFixedSize(intArrayOf(1, 697), DataType.FLOAT32)
        expInputTensor.loadArray(expInput)
        //////////////////////////////////////////////////////////////////////////
        val MethFile = patientRef.child("genomic_methylation.csv")
        val methyInput = extractNumericValuesFromCsvRow(MethFile, intArrayOf(1, 808))
        val methyInputTensor =
            TensorBuffer.createFixedSize(intArrayOf(1, 808), DataType.FLOAT32)
        methyInputTensor.loadArray(methyInput)
        ////////////////////////////////////////////////////////////////////////////////
        val drugCsvFile = drugRef
        val drugInput = extractNumericValuesFromCsvRow(drugCsvFile, intArrayOf(1, 1, 1))
        val drugInputTensor =
            TensorBuffer.createFixedSize(intArrayOf(1, 1, 1), DataType.FLOAT32)
        drugInputTensor.loadArray(drugInput)
        //////////////////////////////////////////////////////////////////////////////////////
        // Download and use the TensorFlow model
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        lateinit var interpreter: Interpreter
        FirebaseModelDownloader.getInstance().getModel(
            "Regrission",
            DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
            conditions
        ).addOnSuccessListener {
            var modelFile = it?.file
            if (modelFile != null) {
                Toast.makeText(
                    this,
                    "Regression Model downloaded successfully!",
                    Toast.LENGTH_LONG
                ).show()
                interpreter = Interpreter(modelFile)
                var outputShape = interpreter.getOutputTensor(0).shape()
                var outputDataType = interpreter.getOutputTensor(0).dataType()
                var outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType)
                val inputs = arrayOf<Any>(
                    expInputTensor.buffer,
                    drugFeatTensor.buffer,
                    mutInputTensor.buffer,
                    methyInputTensor.buffer,
                    drugInputTensor.buffer,
                )
                val outputs = mutableMapOf<Int, Any>(0 to outputBuffer.buffer)
                interpreter.runForMultipleInputsOutputs(inputs, outputs)
                var output = outputBuffer.floatArray[0]
                predictiedIC50.setText(output.toString())
                preditingProgress.visibility=View.GONE
            } else {
                Toast.makeText(
                    this,
                    "Model failed to downloaded.Check your connection.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            saveRecord.setOnClickListener {
            treatmentID=appointmentRef.push().key!!
             val treatment=TreatmentData(treatmentID,)
            }
        }
    }
}
    private fun extractNumericValuesFromCsvRow(csvRef: StorageReference, shape: IntArray): FloatArray {
        val task = csvRef.getBytes(Long.MAX_VALUE)
        while (!task.isComplete); // wait until the task completes
        if (task.isSuccessful) {
            val csvByteArray = task.result
            val csvData = csvByteArray.toString(Charsets.UTF_8)
            val parser = RFC4180ParserBuilder().build() // create an RFC4180Parser
            val row = parser.parseLine(csvData) // parse the first row from the CSV file
            val numericValues = row.filter { it.trim().toFloatOrNull() != null } // filter out the non-numeric values
            val floatArray = numericValues.map { it.trim().toFloat() }.toFloatArray() // convert the numeric values to a float array
            val product = shape.reduce { acc, i -> acc * i } // calculate the product ofthe dimensions of the TensorBuffer shape
            if (floatArray.size > product) { // check if the size of the float array is larger than the product of the dimensions of theTensorBuffer shape
                Log.w("TensorBuffer warning", "The size of the float array returned by extractNumericValuesFromCsvRow is larger than the specified shape of the TensorBuffer. Truncating the array to fit the shape and padding the remaining elements with 0.")
            }
            val reshapedArray = FloatArray(product){1.0f}
            floatArray.take(product).forEachIndexed { index, value ->
                reshapedArray[index] = value
            }
            return reshapedArray // reshape the float array to match the TensorBuffer shape and return it
        } else {
            Log.e("FirebaseStorage", "Error retrieving CSV file: ${task.exception}")
            return FloatArray(0) // return an empty float array if the task is not successful
        }
    }
    //nav_bar
    private fun logout() {
        pref.prefClear()
        moveToLogin()
    }
    private fun moveToLogin() {
        startActivity(Intent(this, Login::class.java).putExtra("hint",R.string.p_email_hint.toString()))
        Toast.makeText(this,R.string.logout, Toast.LENGTH_LONG).show()
        finish()
    }
    private fun appointment() {
        startActivity(Intent(this, Appointments::class.java))
    }

    private fun doctorProfile() {
        startActivity(Intent(this, Profile::class.java))
    }
    private fun patients() {
        startActivity(Intent(this, Patients::class.java))
    }

    private fun model() {
        startActivity(Intent(this, Pre_model::class.java))
    }
    //nav_bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
    private fun init(){
        patientSpinner=findViewById(R.id.patientsSpinner)
        drugSpinner=findViewById(R.id.drugsSpinner)
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        patientSpinner.adapter = adapter
        database.child("Patients").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.clear()
                if (snapshot.exists()){
                    for (patientData in snapshot.children){
                        var patient = patientData.getValue<PatientData>()
                        val patientName="${patient?.firstName} ${patient?.lastName}"
                        adapter.add(patientName)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        drugSpinner=findViewById(R.id.drugsSpinner)
        ArrayAdapter.createFromResource(this,R.array.drugNames,android.R.layout.simple_spinner_item).also {
                adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            drugSpinner.adapter=adapter
        }
        predictBtn=findViewById(R.id.predictBtn)
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        navHeader=navView.getHeaderView(0)
        context=this
        pref= Prefrences(context)
        userName=navHeader.findViewById(R.id.user_name)
        userName.setText(pref.userName)
    }
}
