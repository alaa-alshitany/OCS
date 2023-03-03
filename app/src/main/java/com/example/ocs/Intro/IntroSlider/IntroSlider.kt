package com.example.ocs.Intro.IntroSlider

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
import com.example.ocs.Intro.login.login
import com.example.ocs.R
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentPagerAdapter as FragmentPagerAdapter1


class IntroSlider : AppCompatActivity() {
    lateinit var prefManager: SharedPreferences
    lateinit var activity:Activity
    val pref_show_intro="Intro"

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_slider)
        checkUser()

        var viewPager:ViewPager=findViewById(R.id.viewPager)
        var tabLayout:TabLayout=findViewById(R.id.tabLayout)
        var next_btn: Button =findViewById(R.id.next_btn)

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
    @RequiresApi(Build.VERSION_CODES.S)
    fun checkUser() { // Checking for first time launch
        activity = this
        prefManager = getSharedPreferences("IntroSlider", Context.MODE_PRIVATE)
        if (!prefManager.getBoolean(pref_show_intro, true)) {
           moveToLogin()
        }
    }
    fun moveToLogin(){
        startActivity(Intent(activity, login::class.java))
        finish()
    }
    }
