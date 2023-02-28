package com.example.ocs.Intro

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.ocs.R
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.FragmentPagerAdapter as FragmentPagerAdapter1


class IntroSlider : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_slider)
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
                if(position==adapter.count-1){ next_btn.setText(R.string.get_started) }
                else{next_btn.setText(R.string.next)}
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        }
    }
