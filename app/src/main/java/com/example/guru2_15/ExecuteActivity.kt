package com.example.guru2_15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.get
import org.w3c.dom.Text
import java.util.*

class ExecuteActivity : AppCompatActivity() {

    var selectYear : Int = 0
    var selectMonth : Int = 0
    var selectDay : Int = 0

    var selectYear2 : Int = 0
    var selectMonth2 : Int = 0
    var selectDay2 : Int = 0

    var stHour : Int = 0
    var stMinute : Int = 0

    var fHour : Int = 0
    var fMinute : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("약속 잡기")

        lateinit var btnmake : Button
        lateinit var calendarView : CalendarView
        lateinit var calendarView2 : CalendarView


        val stHour = findViewById<Spinner>(R.id.stHour)
        val stMinute = findViewById<Spinner>(R.id.stMinute)

        val fHour = findViewById<Spinner>(R.id.fHour)
        val fMinute = findViewById<Spinner>(R.id.fMinute)

        stHour.adapter = ArrayAdapter.createFromResource(this, R.array.hour_list, android.R.layout.simple_spinner_dropdown_item)
        stMinute.adapter = ArrayAdapter.createFromResource(this, R.array.minute_list, android.R.layout.simple_spinner_dropdown_item)

        fHour.adapter = ArrayAdapter.createFromResource(this, R.array.hour_list, android.R.layout.simple_spinner_dropdown_item)
        fMinute.adapter = ArrayAdapter.createFromResource(this, R.array.minute_list, android.R.layout.simple_spinner_dropdown_item)

        btnmake = findViewById(R.id.btnmake)
        calendarView = findViewById(R.id.calenderView)
        calendarView2 = findViewById(R.id.calenderView2)


        calendarView.setOnDateChangeListener { view, i, i2, i3 ->
            this.selectYear = i
            this.selectMonth = i2+1
            this.selectDay = i3

        }

        calendarView2.setOnDateChangeListener { view, i, i2, i3 ->
            this.selectYear2 = i
            this.selectMonth2 = i2+1
            this.selectDay2 = i3
        }

        btnmake.setOnClickListener {
            if(selectYear == 0 )
            {
                Toast.makeText(this, "시작 날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
            }
            else if(selectYear2 == 0)
            {
                Toast.makeText(this, "종료 날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(this, ResultActivity::class.java)

                intent.putExtra("stYear", Integer.toString(selectYear))
                intent.putExtra("stMon", Integer.toString(selectMonth))
                intent.putExtra("stDay", Integer.toString(selectDay))

                intent.putExtra("fYear", Integer.toString(selectYear2))
                intent.putExtra("fMon", Integer.toString(selectMonth2))
                intent.putExtra("fDay", Integer.toString(selectDay2))

                intent.putExtra("stHour", stHour.selectedItem.toString())
                intent.putExtra("stMinute", stMinute.selectedItem.toString())

                intent.putExtra("fHour", fHour.selectedItem.toString())
                intent.putExtra("fMinute", fMinute.selectedItem.toString())

//            Log.d("intentday", selectMonth.toString())
//            Log.d("intentday", selectDay.toString())

                startActivity(intent)
            }
        }
    }
}