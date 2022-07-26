package com.example.guru2_15

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MyScheDay : AppCompatActivity (),View.OnClickListener {

    lateinit var monthBtn : Button
    lateinit var weekBtn : Button
    lateinit var dayBtn : Button
    lateinit var addScheFab : FloatingActionButton
    lateinit var pieChart: PieChart
    var arrayColor = arrayListOf<Int>(Color.BLUE,Color.GRAY,Color.GREEN)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sche_day)

        monthBtn = findViewById(R.id.monthBtn)
        weekBtn = findViewById(R.id.weekBtn)
        dayBtn = findViewById(R.id.dayBtn)
        addScheFab = findViewById(R.id.addScheFab)

        monthBtn.setOnClickListener(this)
        weekBtn.setOnClickListener(this)
        dayBtn.setOnClickListener(this)
        addScheFab.setOnClickListener(this)

        pieChart = findViewById(R.id.chart)
        var pieDataSet = PieDataSet(data1(),"좋음 싫음 투표")
        pieDataSet.setColors(arrayColor)
        var pieData = PieData(pieDataSet)
        pieChart.setData(pieData)
        pieChart.invalidate()
    }


    fun data1() : ArrayList<PieEntry>{
        var datavalue = ArrayList<PieEntry>()

        datavalue.add(PieEntry(30.0f,"무응답"))
        datavalue.add(PieEntry(50.0f,"좋음"))
        datavalue.add(PieEntry(20.0f,"싫음"))

        return datavalue
    }
    override fun onClick(view: View?){
        if(view!=null){
            when(view.id){
                R.id.monthBtn -> {
                    var intent = Intent(this, MyScheMonth::class.java)
                    startActivity(intent)
                }
                R.id.weekBtn -> {
                    var intent = Intent(this, MyScheWeek::class.java)
                    startActivity(intent)
                }
                R.id.dayBtn -> {
                    var intent = Intent(this, MyScheDay::class.java)
                    startActivity(intent)
                }
                R.id.addScheFab -> {
                    var intent = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
               }
            }
        }
    }
}