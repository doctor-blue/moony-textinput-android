package com.doctorblue.nonamelibsandroid

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*txt.setOnClickListener {
            val valueTV = TextView(this)
            valueTV.text = "hallo hallo"
            valueTV.id = R.id.SHOW_PATH
            valueTV.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            //txt.addTextView(valueTV)

        }*/
    }
}