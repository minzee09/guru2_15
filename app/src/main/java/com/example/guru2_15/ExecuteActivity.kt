package com.example.guru2_15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import java.util.*

class ExecuteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_execute)
        setTitle("약속 잡기")

        lateinit var btnmake : Button
        lateinit var calendarView : CalendarView

        var selectYear : Int = 0
        var selectMonth : Int = 0
        var selectDay : Int = 0
        var selectHour : Int = 0
        var selectMinute : Int = 0

        btnmake = findViewById(R.id.btnmake)

//        calendarView.setOnDateChangeListener { calendarView, year, month, day ->
//            selectYear = year
//            selectMonth = month
//            selectDay = day
//        }


        btnmake.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("stMon", Integer.toString(selectMonth))
            intent.putExtra("stDay", Integer.toString(selectDay))
            intent.putExtra("stYear", Integer.toString(selectYear))
            startActivity(intent)

        }
    }
}