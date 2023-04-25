package com.example.ocs.Intro.Login
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ocs.R
import java.util.regex.Matcher
import java.util.regex.Pattern


class Register : AppCompatActivity() {
    lateinit var continueBtn: TextView
    lateinit var firstNameEdt:EditText
    lateinit var lastNameEdt:EditText
    lateinit var emailEdt:EditText
    lateinit var phoneEdt:EditText
    lateinit var genderRadio:RadioGroup
    lateinit var radioButton: RadioButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
         bindingItems()

        continueBtn.setOnClickListener { moveToContinueRegister() }
    }
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
    private fun moveToContinueRegister() {
        var selectedGender:Int=genderRadio!!.checkedRadioButtonId
        radioButton = findViewById(selectedGender)
        if (validateInputData()){
            val intent = Intent(this,RegisterContinue::class.java)
            intent.putExtra("first name",firstNameEdt.text.toString())
            intent.putExtra("last name",lastNameEdt.text.toString())
            intent.putExtra("email", emailEdt.text.toString())
            intent.putExtra("phone", phoneEdt.text.toString())
            intent.putExtra("gender",radioButton.text.toString())
            startActivity(intent)
        }
    }

    private fun bindingItems(){
        continueBtn=findViewById(R.id.continue_register_btn)
        firstNameEdt=findViewById(R.id.firstName)
        lastNameEdt=findViewById(R.id.lastName)
        emailEdt=findViewById(R.id.email)
        phoneEdt=findViewById(R.id.phone)
        genderRadio=findViewById(R.id.gender)

    }

    private fun validateInputData() :Boolean{
        var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        var phonePattern="^01[0125][0-9]{8}\$"
         if(firstNameEdt.text.toString().isNotEmpty() && lastNameEdt.text.toString().isNotEmpty()
             && emailEdt.text.toString().trim().matches(emailPattern.toRegex())
             && emailEdt.text.toString().isNotEmpty() && phoneEdt.text.toString().isNotEmpty()
             && phoneEdt.text.toString().length ==11 && phoneEdt.text.toString().trim().matches(phonePattern.toRegex())){
             return true
         }else{
             if(emailEdt.text.toString().trim().isEmpty() && firstNameEdt.text.toString().isEmpty()
                 &&(lastNameEdt.text.toString().isEmpty() && phoneEdt.text.toString().isEmpty())){
                 Toast.makeText(this,R.string.emptyFields,Toast.LENGTH_LONG).show()
             }


             if (emailEdt.text.toString().trim().isEmpty())
                 Toast.makeText(this, R.string.emptyEmail, Toast.LENGTH_LONG).show()
             else if (!emailEdt.text.toString().trim().matches(emailPattern.toRegex()))
                 Toast.makeText(this, R.string.notValidEmail, Toast.LENGTH_LONG).show()


             if (firstNameEdt.text.toString().isEmpty())
                 Toast.makeText(this, R.string.emptyFName, Toast.LENGTH_LONG).show()

             if (lastNameEdt.text.toString().isEmpty())
                 Toast.makeText(this, R.string.emptyLName, Toast.LENGTH_LONG).show()

             if (phoneEdt.text.toString().isEmpty())
                 Toast.makeText(this, R.string.emptyPhone, Toast.LENGTH_LONG).show()
             else if (phoneEdt.text.toString().length != 11 || !phoneEdt.text.toString().trim().matches(phonePattern.toRegex()))
                 Toast.makeText(this, R.string.notValidNumber, Toast.LENGTH_LONG).show()

             return false
         }

     }
}