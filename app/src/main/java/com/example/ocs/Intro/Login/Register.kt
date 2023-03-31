package com.example.ocs.Intro.Login
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.ocs.R


class Register : AppCompatActivity() {
    lateinit var continue_btn: TextView
    lateinit var firstName_edt:EditText
    lateinit var lastName_edt:EditText
    lateinit var email_edt:EditText
    lateinit var phone_edt:EditText
    lateinit var genderRadio:RadioGroup
    lateinit var radioButton: RadioButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
         bindingItems()

        continue_btn.setOnClickListener { moveToContinueRegister() }
    }
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
    private fun moveToContinueRegister() {
        var selectedGender:Int=genderRadio!!.checkedRadioButtonId
        radioButton = findViewById(selectedGender)
        if (validateInputData()){
            val intent = Intent(this,RegisterContinue::class.java)
            intent.putExtra("first name",firstName_edt.text.toString())
            intent.putExtra("last name",lastName_edt.text.toString())
            intent.putExtra("email", email_edt.text.toString())
            intent.putExtra("phone", phone_edt.text.toString())
            intent.putExtra("gender",radioButton.text.toString())
            startActivity(intent)
        }
    }

    private fun bindingItems(){
        continue_btn=findViewById(R.id.continue_register_btn)
        firstName_edt=findViewById(R.id.firstName)
        lastName_edt=findViewById(R.id.lastName)
        email_edt=findViewById(R.id.email)
        phone_edt=findViewById(R.id.phone)
        genderRadio=findViewById(R.id.gender)

    }
     private fun validateInputData() :Boolean{
        var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
         if(firstName_edt.text.toString().isNotEmpty() && lastName_edt.text.toString().isNotEmpty()
             && email_edt.text.toString().trim().matches(emailPattern.toRegex())
             && email_edt.text.toString().isNotEmpty() && phone_edt.text.toString().isNotEmpty()
             && phone_edt.text.toString().length ==11 && genderRadio.getCheckedRadioButtonId() == -1){
             return true
         }else{
             if(email_edt.text.toString().trim().isEmpty() && firstName_edt.text.toString().isEmpty()
                 &&(lastName_edt.text.toString().isEmpty() && phone_edt.text.toString().isEmpty())){
                 Toast.makeText(this,R.string.emptyFields,Toast.LENGTH_LONG).show()
             }


             if (email_edt.text.toString().trim().isEmpty())
                 Toast.makeText(this, R.string.emptyEmail, Toast.LENGTH_LONG).show()
             else if (!email_edt.text.toString().trim().matches(emailPattern.toRegex()))
                 Toast.makeText(this, R.string.notValidEmail, Toast.LENGTH_LONG).show()


             if (firstName_edt.text.toString().isEmpty())
                 Toast.makeText(this, R.string.emptyFName, Toast.LENGTH_LONG).show()

             if (lastName_edt.text.toString().isEmpty())
                 Toast.makeText(this, R.string.emptyLName, Toast.LENGTH_LONG).show()

             if (phone_edt.text.toString().isEmpty())
                 Toast.makeText(this, R.string.emptyPhone, Toast.LENGTH_LONG).show()
             else if (phone_edt.text.toString().length != 11)
                 Toast.makeText(this, R.string.notValidNumber, Toast.LENGTH_LONG).show()

             return false
         }

     }
}