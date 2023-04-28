package com.example.ocs.Intro.IntroSlider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ocs.R

class SliderItemFragment : Fragment() {

   val argPosition:String="slider-position"
    //array for titles
    val pageTitle:IntArray= intArrayOf(
        R.string.slider1_title,
        R.string.slider2_title,
        R.string.slider3_title
    )
    //array for description
    val pageDescription:IntArray= intArrayOf(
        R.string.slider1_description,
        R.string.slider2_description,
        R.string.slider3_description
    )
    //array for images
    val pageImage:IntArray= intArrayOf(
        R.drawable.clinics_service,
        R.drawable.rays_service,
        R.drawable.tests_service)
    var position:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       position=if(arguments !=null) requireArguments().getInt(argPosition) else TODO()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       var title:TextView=view.findViewById(R.id.pageTitle)
        var desc:TextView=view.findViewById(R.id.pageDescription)
        var image:ImageView=view.findViewById(R.id.pageImage)

        title.setText(pageTitle[position])
        desc.setText(pageDescription[position])
        image.setImageResource(pageImage[position])
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slider_item, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            SliderItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(argPosition, position)
                }
            }
    }
}