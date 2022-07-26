package com.example.guru2_15

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setTitle("약속 잡기")

        lateinit var btnRe : Button

        lateinit var stYear : TextView
        lateinit var stMon : TextView
        lateinit var stDay : TextView
        lateinit var stHour : TextView
        lateinit var stMinute: TextView
        lateinit var fMon : TextView
        lateinit var fDay : TextView
        lateinit var fHour : TextView
        lateinit var fMinute : TextView

//        val secondintent = intent
//
//        if(.hasExtra("stYear"))
//        {
//            stYear.text = secondintent.getStringExtra("stYear")
//        }
//
//        if(secondintent.hasExtra("stMon"))
//        {
//            stMon.text = secondintent.getStringExtra("stMon")
//        }
//
//        if(secondintent.hasExtra("stDay"))
//        {
//            stDay.text = secondintent.getStringExtra("stDay")
//        }

        btnRe = findViewById(R.id.btnRe)

        btnRe.setOnClickListener {
            finish()
        }
    }

}