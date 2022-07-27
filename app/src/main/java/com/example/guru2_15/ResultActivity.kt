package com.example.guru2_15

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

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

        lateinit var fYear : TextView
        lateinit var fMon : TextView
        lateinit var fDay : TextView
        lateinit var fHour : TextView
        lateinit var fMinute : TextView

        stYear = findViewById(R.id.stYear)
        stMon = findViewById(R.id.stMon)
        stDay = findViewById(R.id.stDay)
        stHour = findViewById(R.id.stHour)
        stMinute = findViewById(R.id.stMinute)

        fYear = findViewById(R.id.fYear)
        fMon = findViewById(R.id.fMon)
        fDay = findViewById(R.id.fDay)
        fHour = findViewById(R.id.fHour)
        fMinute = findViewById(R.id.fMinute)


        stYear.setText(intent.getStringExtra("stYear"))
        stMon.setText(intent.getStringExtra("stMon"))
        stDay.setText(intent.getStringExtra("stDay"))
        stHour.setText(intent.getStringExtra("stHour"))
        stMinute.setText(intent.getStringExtra("stMinute"))

        fYear.setText(intent.getStringExtra("fYear"))
        fMon.setText(intent.getStringExtra("fMon"))
        fDay.setText(intent.getStringExtra("fDay"))
        fHour.setText(intent.getStringExtra("fHour"))
        fMinute.setText(intent.getStringExtra("fMinute"))

        btnRe = findViewById(R.id.btnRe)

        btnRe.setOnClickListener {
            finish()
        }
    }

}