package com.example.ocs.Intro.IntroSlider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.ocs.R

class splashScreen : AppCompatActivity() {
    lateinit var handler :Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)
       animate()
       splashTime()
    }
    fun animate(){
        val logoImage: ImageView = findViewById(R.id.logo)
        val appName:TextView=findViewById(R.id.ocs)
        val appDesc:TextView=findViewById(R.id.ocs_desc)
        val splashAnimation = AnimationUtils.loadAnimation(this, R.anim.side_splash)
        logoImage.startAnimation(splashAnimation)
        appName.startAnimation(splashAnimation)
        appDesc.startAnimation(splashAnimation)
    }
    fun splashTime(){
        handler= Handler(Looper.getMainLooper())
        handler.postDelayed({
            var splash_slider= Intent(this, IntroSlider::class.java)
            startActivity(splash_slider)
            finish()
        },2200)
    }
}
