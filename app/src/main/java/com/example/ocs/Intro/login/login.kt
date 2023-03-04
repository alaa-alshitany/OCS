package com.example.ocs.Intro.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ocs.Intro.Home.services
import com.example.ocs.R


class login : AppCompatActivity() {
    lateinit var register_btn:Button
    lateinit var activity: Activity
    lateinit var login_btn:Button
    lateinit var email_edt:EditText
    lateinit var login_textView:TextView
    lateinit var newUser_textView:TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email_edt=findViewById(R.id.email)
        login_textView=findViewById(R.id.login_text_view)
        register_btn=findViewById(R.id.register_btn)
        login_btn=findViewById(R.id.login_btn)
        newUser_textView=findViewById(R.id.newUser_txtView)

        val intent=intent
        var hint:String =intent.getStringExtra("email_hint").toString()

        if (hint != null) {
            if (hint.equals(R.string.d_email_hint.toString())){
                email_edt.setHint(R.string.d_email_hint)
                login_textView.setText(R.string.d_login)
                register_btn.setText(null)
                newUser_textView.setText(null)

            }else if(hint.equals(R.string.a_email_hint.toString())){
                email_edt.setHint(R.string.a_email_hint)
                login_textView.setText(R.string.a_login)
                register_btn.setText(null)
                newUser_textView.setText(null)
            }else{
                email_edt.setHint(R.string.p_email_hint)
            }
        }

        register_btn.setOnClickListener { moveToRegister()}


        login_btn.setOnClickListener { moveToHome() }
    }

    private fun moveToHome() {
     activity=this
        startActivity(Intent(activity,services::class.java))
        finish()
    }

    private fun moveToRegister() {
        activity=this
        startActivity(Intent(activity,register::class.java))
       finish()
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