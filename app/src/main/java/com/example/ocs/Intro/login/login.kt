package com.example.ocs.Intro.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.ocs.Intro.patient.services.services
import com.example.ocs.Intro.admin.profile
import com.example.ocs.R
import com.google.android.material.textfield.TextInputLayout


class login : AppCompatActivity() {
    lateinit var register_btn:Button
    lateinit var activity: Activity
    lateinit var login_btn:Button
    lateinit var email_edt:EditText
    lateinit var login_textView:TextView
    lateinit var newUser_textView:TextView
    lateinit var login_img:ImageView
    lateinit var email_layout:TextInputLayout
    lateinit var forgetpass_btn:Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bindingItems()

        val intent=intent
        var hint:String =intent.getStringExtra("email_hint").toString()

        if (hint != null) {
            if (hint.equals(R.string.d_email_hint.toString())){
                doctorLogin()

            }else if(hint.equals(R.string.a_email_hint.toString())){
                adminLogin()

            }else{
                patientLogin()

            }
        }

        register_btn.setOnClickListener { moveToRegister()}


        //login_btn.setOnClickListener { moveToHome() }
    }

    private fun moveToHome() {
     activity=this
        startActivity(Intent(activity, services::class.java))
        finish()
    }

    private fun moveToRegister() {
        activity=this
        startActivity(Intent(activity,register::class.java))
       finish()

    }
    private fun doctorLogin(){
        email_edt.setHint(R.string.d_email_hint)
        login_textView.setText(R.string.d_login)
        register_btn.visibility = View.INVISIBLE
        newUser_textView.visibility = View.INVISIBLE
    }
    private fun adminLogin(){
        email_edt.setHint(R.string.a_email_hint)
        login_textView.setText(R.string.a_login)
        register_btn.visibility = View.INVISIBLE
        newUser_textView.visibility = View.INVISIBLE
        login_img.setImageResource(R.drawable.baseline_admin_panel_settings_24)
        forgetpass_btn.visibility=View.INVISIBLE
        email_layout.isPasswordVisibilityToggleEnabled=true
        login_btn.setOnClickListener { startActivity(Intent(this,profile::class.java)) }
        //email_layout.setPasswordVisibilityToggleTintList()
    }
    private fun patientLogin(){
        email_edt.setHint(R.string.p_email_hint)
    }
    private fun bindingItems(){
        email_edt=findViewById(R.id.email)
        login_textView=findViewById(R.id.login_text_view)
        register_btn=findViewById(R.id.register_btn)
        login_btn=findViewById(R.id.login_btn)
        newUser_textView=findViewById(R.id.newUser_txtView)
        login_img=findViewById(R.id.login_img)
        email_layout=findViewById(R.id.email_input_layout)
        forgetpass_btn=findViewById(R.id.forgetPass_btn)
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