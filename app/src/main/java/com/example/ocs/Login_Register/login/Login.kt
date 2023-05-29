        package com.example.ocs.Login_Register.login
        import android.app.Activity
        import android.content.Context
        import android.content.Intent
        import android.net.ConnectivityManager
        import android.net.NetworkCapabilities
        import android.os.Build
        import androidx.appcompat.app.AppCompatActivity
        import android.os.Bundle
        import android.util.Log
        import android.view.Menu
        import android.view.MenuItem
        import android.view.View
        import android.widget.Button
        import android.widget.EditText
        import android.widget.ImageView
        import android.widget.TextView
        /////////////////////////////////////////////////////
        import com.opencsv.RFC4180ParserBuilder
        ////////////////////////////////////////////////////
        import android.widget.Toast
        import com.example.ocs.Login_Register.forgetPassword.ForgetPassword
        import com.example.ocs.Patient.PatientData
        import com.example.ocs.Login_Register.register.Register
        import com.example.ocs.Admin.AdminData
        import com.example.ocs.Patient.services.Services
        import com.example.ocs.R
        import com.example.ocs.Admin.Dashboard.Dashboard
        import com.example.ocs.Admin.Doctors.DoctorData
        import com.example.ocs.doctor.DoctorDashboard
        import com.google.android.material.textfield.TextInputLayout
        import com.google.firebase.auth.FirebaseAuth
        import com.google.firebase.auth.ktx.auth
        import com.google.firebase.database.*
        import com.google.firebase.database.ktx.getValue
        import com.google.firebase.ktx.Firebase
        //import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
        //import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
        //import com.google.firebase.ml.custom.*
        import com.google.firebase.storage.StorageReference

        class Login : AppCompatActivity() {
            private lateinit var registerBtn:Button
            private lateinit var activity: Activity
            private lateinit var loginBtn:Button
            private lateinit var email_edt:EditText
            private lateinit var loginTextview:TextView
            private lateinit var newuserTextview:TextView
            private lateinit var loginImg:ImageView
            private lateinit var emailLayout:TextInputLayout
            private lateinit var forgetPasswordBtn:Button
            private lateinit var passwordEdt:EditText
            private lateinit var context: Context
            private lateinit var pref: Prefrences
            private lateinit var intent2:Intent
            private lateinit var patientID:String
            private lateinit var auth: FirebaseAuth
            private val database:DatabaseReference=FirebaseDatabase.getInstance().reference

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_login)
                supportActionBar!!.elevation= 0F
                init()
                //getIntentExtra()
                registerBtn.setOnClickListener { register()}
                forgetPasswordBtn.setOnClickListener { forgetPassword() }
            }
            private fun checkInternet(context: Context) : Boolean{
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    // Returns a Network object corresponding to
                    // the currently active default data network.
                    val network = connectivityManager.activeNetwork ?: return false
                    // Representation of the capabilities of an active network.
                    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
                    return when {
                        // Indicates this network uses a Wi-Fi transport,
                        // or WiFi has network connectivity
                        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                        // Indicates this network uses a Cellular transport. or
                        // Cellular has network connectivity
                        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                        // else return false
                        else -> false
                    }
                } else {
                    // if the android version is below M
                    @Suppress("DEPRECATION") val networkInfo =
                        connectivityManager.activeNetworkInfo ?: return false
                    @Suppress("DEPRECATION")
                    return networkInfo.isConnected
                }
            }
            private fun forgetPassword() {
                startActivity(Intent(activity, ForgetPassword::class.java))
            }
            private fun getIntentExtra(){
                var hint:String =intent.getStringExtra("email_hint").toString()
                when(hint){
                    R.string.d_email_hint.toString() -> doctorLogin()
                    R.string.a_email_hint.toString() -> adminLogin()
                    R.string.p_email_hint.toString() -> patientLogin()
                    else -> {
                        patientLogin()
                    }
                }
            }
            private fun home() {
                startActivity(Intent(activity, Services::class.java))
                finish()
            }
            private fun register() {
                startActivity(Intent(activity, Register::class.java))
            }
            private fun doctorLogin() {
                email_edt.setHint(R.string.d_email_hint)
                loginTextview.setText(R.string.d_login)
                registerBtn.visibility = View.INVISIBLE
                newuserTextview.visibility = View.INVISIBLE
                forgetPasswordBtn.visibility=View.INVISIBLE

                loginBtn.setOnClickListener {
                    if (checkInternet(this)){
                        checkEmptyData(email_edt,passwordEdt)
                        readDoctorData(email_edt.text.toString(),passwordEdt.text.toString())
                    }else {
                        Toast.makeText(applicationContext, R.string.internetError, Toast.LENGTH_LONG).show()
                    }
                               /*
                                val storageRef = FirebaseStorage.getInstance().getReference("Test_Data/")
                                val file1Ref =storageRef.child("10172943.hkl")
                                val localFile = File.createTempFile("temp", ".hkl")
                                file1Ref.getFile(localFile).addOnSuccessListener {
                                    // Read the contents of the file into a byte array
                                    val bytes = localFile.readBytes()
                                    val floatArray = FloatArray(75)
                                    ByteBuffer.wrap(bytes).asFloatBuffer().get(floatArray)
                                    for (i in floatArray.indices) {
                                        if (floatArray[i].isNaN()) {
                                            floatArray[i] = 1f
                                        }
                                    }
                                    val drugFeatTensor = TensorBuffer.createFixedSize(intArrayOf(1, 1, 75), DataType.FLOAT32)
                                    drugFeatTensor.loadArray(floatArray)

                                    //val drugInput = extractNumericValuesFromCsvRow(file1Ref, intArrayOf(1, 697))
                                    //  val drugInputTensor = TensorBuffer.createFixedSize(intArrayOf(1, 697), DataType.FLOAT32)
                                    //  drugInputTensor.loadArray(drugInput)
                                    //for (i in drugInput.indices) {
                                    //     Log.d("druginput", "drugInput[$i]: ${drugInput[i]}")
                                    //   }
                                    ////////////////////////////////////////////////////////////////////////////////////
                                    val file2Ref = storageRef.child("mutation.csv")
                                    val mutInput = extractNumericValuesFromCsvRow(
                                        file2Ref,
                                        intArrayOf(1, 1, 34673, 1)
                                    )
                                    val mutInputTensor = TensorBuffer.createFixedSize(
                                        intArrayOf(1, 1, 34673, 1),
                                        DataType.FLOAT32
                                    )
                                    mutInputTensor.loadArray(mutInput)
                                    for (i in mutInput.indices) {
                                        Log.d("mutInput", "mutInput[$i]: ${mutInput[i]}")
                                    }
                                    //////////////////////////////////////////////////////////////////////////
                                    val file3Ref = storageRef.child("genomic.csv")
                                    val expInput = extractNumericValuesFromCsvRow(
                                        file3Ref,
                                        intArrayOf(1,697)
                                    )
                                    val expInputTensor = TensorBuffer.createFixedSize(
                                        intArrayOf(1, 697),
                                        DataType.FLOAT32
                                    )
                                    expInputTensor.loadArray(expInput)
                                    ////////////////////////////////////////////////////////////////////////////////
                                    val file4Ref = storageRef.child("methylation.csv")
                                    val methyInput =
                                        extractNumericValuesFromCsvRow(file4Ref, intArrayOf(1,808))
                                    val methyInputTensor = TensorBuffer.createFixedSize(
                                        intArrayOf(1, 808),
                                        DataType.FLOAT32
                                    )
                                    methyInputTensor.loadArray(methyInput)
                                    //////////////////////////////////////////////////////////////////////////////////
                                    val file5Ref = storageRef.child("drug1.csv")
                                    val drugInput = extractNumericValuesFromCsvRow(
                                        file5Ref,
                                        intArrayOf(1, 1, 1)
                                    )
                                    val drugInputTensor = TensorBuffer.createFixedSize(
                                        intArrayOf(1, 1, 1),
                                        DataType.FLOAT32
                                    )
                                    drugInputTensor.loadArray(drugInput)
                                    //////////////////////////////////////////////////////////////////////////////////////
                                    Log.d("TFLite version", TensorFlowLite.runtimeVersion())
                                    /////////////////////////////////////////////////////////////////////////////////////
                                    // Download and use the TensorFlow model
                                    val conditions = CustomModelDownloadConditions.Builder()
                                        .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
                                        .build()
                                    lateinit var interpreter: Interpreter
                                    FirebaseModelDownloader.getInstance().getModel(
                                        "Deep_CDR",
                                        DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
                                        conditions
                                    )
                                        .addOnSuccessListener {
                                            val modelFile = it?.file
                                            if (modelFile != null) {
                                                Log.d("this", "model downloaded successfully")
                                                interpreter = Interpreter(modelFile)

                                                for (i in 0..4) {
                                                    Log.d(
                                                        "input tensor $i shape",
                                                        interpreter.getInputTensor(i).shape()
                                                            .contentToString()
                                                    )
                                                    Log.d(
                                                        "input tensor $i data type",
                                                        interpreter.getInputTensor(i)
                                                            .dataType().name
                                                    )
                                                    Log.d(
                                                        "input tensor $i name",
                                                        interpreter.getInputTensor(i).name()
                                                    )
                                                }
                                                Log.d(
                                                    "output tensor shape",
                                                    interpreter.getOutputTensor(0).shape()
                                                        .contentToString()
                                                )
                                                Log.d(
                                                    "output tensor data type",
                                                    interpreter.getOutputTensor(0).dataType().name
                                                )


                                                val outputShape =
                                                    interpreter.getOutputTensor(0).shape()
                                                val outputDataType =
                                                    interpreter.getOutputTensor(0).dataType()
                                                val outputBuffer =
                                                    TensorBuffer.createFixedSize(
                                                        outputShape,
                                                        outputDataType
                                                    )

                                                val inputs = arrayOf<Any>(
                                                    expInputTensor.buffer,
                                                    drugFeatTensor.buffer,
                                                    mutInputTensor.buffer,
                                                    methyInputTensor.buffer,
                                                    drugInputTensor.buffer,

                                                    )
                                                val outputs =
                                                    mutableMapOf<Int, Any>(0 to outputBuffer.buffer)

                                                interpreter.runForMultipleInputsOutputs(
                                                    inputs,
                                                    outputs
                                                )
                                                val output = outputBuffer.floatArray[0]
                                                val roundedOutput = Math.round(output *1000.0)/1000.0
                                                val outputBinary =
                                                    if (roundedOutput >= 0.5) 1 else 0
                                                if (outputBinary == 1) {
                                                    Log.d(
                                                        "Drug is Resistance for patient with percentage:  ",
                                                        "${roundedOutput * 100} %"
                                                    )
                                                } else {
                                                    Log.d(
                                                        "Drug is Sensitive for patient with percentage:  ",
                                                        "${roundedOutput * 100} %"
                                                    )
                                                }

                                            }
                                        }
                                }// Run the model
                } else {
                    Log.d("this", "model failed to downloaded ")
          */       }
            }
            private fun readDoctorData(email: String,password: String) {
                val auth = Firebase.auth
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            var queryDoctor: Query = database.child("Doctors").orderByChild("email").equalTo(email)
                            queryDoctor.addListenerForSingleValueEvent(object: ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        for (item in snapshot.children) {
                                            var doctor = item.getValue<DoctorData>()
                                            if (doctor?.password.equals(password)) {
                                                pref.userName = "DR/ ${doctor?.firstName} ${doctor?.lastName}"
                                                pref.prefStatus = true
                                                pref.prefLevel = "doctor"
                                                pref.prefID = doctor?.id
                                                Toast.makeText(context, R.string.login_success, Toast.LENGTH_LONG).show()
                                                doctorDashboard()
                                            } else {
                                                passwordEdt.setError(getText(R.string.login_failed))
                                            }
                                        }
                                    }else{
                                        email_edt.setError(getText(R.string.userNotFound))
                                        Toast.makeText(applicationContext,R.string.userNotFound,Toast.LENGTH_LONG).show()
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
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
            private fun adminLogin() {
            email_edt.setHint(R.string.a_email_hint)
            loginTextview.setText(R.string.a_login)
            registerBtn.visibility = View.INVISIBLE
            newuserTextview.visibility = View.INVISIBLE
            loginImg.setImageResource(R.drawable.baseline_admin_panel_settings_24)
            loginImg.layoutParams.height=300
            loginBtn.layoutParams.height=100
            forgetPasswordBtn.visibility = View.INVISIBLE
            emailLayout.isPasswordVisibilityToggleEnabled = true
            loginBtn.setOnClickListener {
            if (checkInternet(this)){
            checkEmptyData(email_edt,passwordEdt)
            readAdminData(email_edt.text.toString(),passwordEdt.text.toString())
            }else{
            Toast.makeText(applicationContext,R.string.internetError,Toast.LENGTH_LONG).show()
            }
            }
            }
            private fun adminDashboard() {
            startActivity(Intent(activity, Dashboard::class.java))
            }
            private fun doctorDashboard(){
                startActivity(Intent(activity,DoctorDashboard::class.java))
            }
            private fun patientLogin(){
            email_edt.setHint(R.string.p_email_hint)
            loginBtn.setOnClickListener {
            if(checkInternet(this)){
            checkEmptyData(email_edt,passwordEdt)
            readPatientData(email_edt.text.toString(),passwordEdt.text.toString())
            }else{
            Toast.makeText(applicationContext,R.string.internetError,Toast.LENGTH_LONG).show()
            }}
            }
            private fun readPatientData(email: String,password: String) {
            var queryPatient: Query = database.child("Patients").orderByChild("email").equalTo(email)
            queryPatient.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
            for (item in snapshot.children) {
            var patient = item.getValue<PatientData>()
            if (patient != null) {
                if (patient.password.equals(password)) {
                   patientID= patient?.id.toString()
                    pref.prefStatus = true
                    pref.prefLevel="patient"
                    pref.prefID=patientID
                    pref.userName=patient?.firstName.toString().plus(" ${patient?.lastName.toString()}")
                    Toast.makeText(context, R.string.login_success, Toast.LENGTH_LONG).show()
                    home()
                }else{
                    passwordEdt.setError(getText(R.string.login_failed))
                }
            }
            }
            }else{
            email_edt.setError(getText(R.string.userNotFound))
            Toast.makeText(applicationContext,R.string.userNotFound,Toast.LENGTH_LONG).show()
            }
            }
            override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
            }
            })
            }
            private fun readAdminData(code: String,password: String) {
            if (checkInternet(this)) {
            var queryAdmin: Query = database.child("Admins").orderByChild("code").equalTo(code)
            queryAdmin.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
            for (item in snapshot.children) {
                var admin = item.getValue<AdminData>()
                if (admin?.password.equals(password)) {
                    pref.userName = admin?.name
                    pref.prefStatus = true
                    pref.prefLevel = "admin"
                    pref.prefID = admin?.id
                    Toast.makeText(context, R.string.login_success, Toast.LENGTH_LONG).show()
                    adminDashboard()
                } else {
                    passwordEdt.setError(getText(R.string.login_failed))
                }
            }
            }else{
            email_edt.setError(getText(R.string.userNotFound))
            Toast.makeText(applicationContext,R.string.userNotFound,Toast.LENGTH_LONG).show()
            }
            }
            override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
            }
            })
            } else {
            Toast.makeText(applicationContext, R.string.internetError, Toast.LENGTH_LONG).show()
            }
            }
            override fun onStart(){
            super.onStart()
            if (pref.prefStatus){
            when(pref.prefLevel){
            "patient" -> home()
            "admin" ->adminDashboard()
            "doctor" -> doctorDashboard()
            }
            }
            }
            private fun checkEmptyData(email:EditText, password:EditText){
            if (email.text.toString().isEmpty() && password.text.toString().isEmpty()){
            email.setError(getText(R.string.requird))
            password.setError(getText(R.string.requird))
            Toast.makeText(this,R.string.emptyAllData,Toast.LENGTH_LONG).show()
            }

            else if (email.text.toString().isEmpty()){
            email.setError(getText(R.string.requird))
            Toast.makeText(this,R.string.emptyEmail,Toast.LENGTH_LONG).show()
            }
            else if(password.text.toString().isEmpty()){
            password.setError(getText(R.string.requird))
            Toast.makeText(this,R.string.emptyPassword,Toast.LENGTH_LONG).show()
            }
            }
            private fun init(){
                getIntentExtra()
            email_edt=findViewById(R.id.email)
            loginTextview=findViewById(R.id.login_text_view)
            registerBtn=findViewById(R.id.register_btn)
            loginBtn=findViewById(R.id.login_btn)
            newuserTextview=findViewById(R.id.newUser_txtView)
            loginImg=findViewById(R.id.login_img)
            emailLayout=findViewById(R.id.email_input_layout)
            forgetPasswordBtn=findViewById(R.id.forgetPass_btn)
            passwordEdt=findViewById(R.id.password_edt)
            context=this
            pref= Prefrences(context)
            intent2=intent
            activity=this
            auth = FirebaseAuth.getInstance()
            }
            override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu,menu)
            return super.onCreateOptionsMenu(menu)
            }
            override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id=item.itemId
            if (id == R.id.doctor) {
            val intent = Intent(this, this::class.java)
            intent.putExtra("email_hint",R.string.d_email_hint.toString())
            this.startActivity(intent)
            return true
            }

            if (id == R.id.admin) {
            val intent = Intent(this, this::class.java)
            intent.putExtra("email_hint",R.string.a_email_hint.toString())
            this.startActivity(intent)
            return true
            }
            if(id==R.id.patient){
            val intent = Intent(this, this::class.java)
            intent.putExtra("email_hint",R.string.p_email_hint.toString())
            this.startActivity(intent)
            return true
            }
            return super.onOptionsItemSelected(item)
            }
        }