package com.example.guru2_15

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MyScheMonth : AppCompatActivity(),View.OnClickListener  {
    var userID: String = "userID"
    lateinit var calendarView: CalendarView
    lateinit var addScheFab : Button
    lateinit var monthBtn : Button
    lateinit var weekBtn : Button
    lateinit var dayBtn : Button

    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sche_month)

        // UI값 생성
        calendarView=findViewById(R.id.calendarView)
        addScheFab = findViewById(R.id.addScheFab)
        monthBtn = findViewById(R.id.monthBtn)
        weekBtn = findViewById(R.id.weekBtn)
        dayBtn = findViewById(R.id.dayBtn)

        monthBtn.setOnClickListener(this)
        weekBtn.setOnClickListener(this)
        dayBtn.setOnClickListener(this)
        addScheFab.setOnClickListener(this)

        dbManager = DBManager(this, "schedule", null, 1)
        sqlitedb = dbManager.readableDatabase

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val dialog = scheDialog(this)
            dialog.showDialog()
            dialog.setOnClickListener(object : scheDialog.OnDialogClickListener{
                override fun onClicked(name: String)
                {
                    var info = dialog.findViewById<TextView>(R.id.infoTv)
                    var date = "${year}년 ${month}월 ${dayOfMonth}일"
                    var cursor : Cursor
                    cursor = sqlitedb.rawQuery("SELECT * FROM schedule WHERE name = '"+date+"';",null)
                    if(cursor==null){
                        info.text="일정 없음"
                    }
                    else{
                        lateinit var sName:String
                        lateinit var sShour:String
                        lateinit var sSMinute:String

                        while (cursor.moveToNext()){
                            sName = cursor.getString(cursor.getColumnIndexOrThrow("Sname")).toString()
                            sShour = cursor.getString(cursor.getColumnIndexOrThrow("SShour")).toString()
                            sSMinute = cursor.getString(cursor.getColumnIndexOrThrow("SSminute")).toString()
                        }

                        info.text = "${sName}스케줄 시간 = ${sShour}: ${sSMinute}"
                    }

                }
            })
        }



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