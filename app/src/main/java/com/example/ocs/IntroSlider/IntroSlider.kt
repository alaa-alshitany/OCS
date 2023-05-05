package com.example.ocs.IntroSlider

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.ocs.Login_Register.login.Login
import com.example.ocs.R
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentPagerAdapter as FragmentPagerAdapter1


class IntroSlider : AppCompatActivity() {
    lateinit var prefManager: SharedPreferences
    lateinit var viewPager:ViewPager
    lateinit var activity:Activity
    lateinit var tabLayout:TabLayout
    lateinit var next_btn: Button
    val pref_show_intro="Intro"

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_slider)

        checkUser()
        init()


        var adapter: SliderAdapter = SliderAdapter(
            supportFragmentManager,
            FragmentPagerAdapter1.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        next_btn.setOnClickListener{
            if (viewPager.currentItem<adapter.count){
                viewPager.currentItem+=1
            }
        }

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if(position==adapter.count-1){
                    next_btn.setText(R.string.get_started)

                    next_btn.setOnClickListener {
                        moveToLogin()
                        val editor=prefManager.edit()
                        editor.putBoolean(pref_show_intro,false)
                        editor.apply()
                    }

                }
                else{next_btn.setText(R.string.next)}
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        }
    fun init(){
        viewPager=findViewById(R.id.viewPager)
        tabLayout=findViewById(R.id.tabLayout)
        next_btn=findViewById(R.id.next_btn)

    }
    @RequiresApi(Build.VERSION_CODES.S)
    fun checkUser() { // Checking for first time launch
        activity = this
        prefManager = getSharedPreferences("IntroSlider", Context.MODE_PRIVATE)
        if (!prefManager.getBoolean(pref_show_intro, true)) {
            moveToLogin()
        }
    }
    fun moveToLogin(){
        startActivity(Intent(activity, Login::class.java).putExtra("email_hint",R.string.p_email_hint.toString()))
        finish()
    }
}
