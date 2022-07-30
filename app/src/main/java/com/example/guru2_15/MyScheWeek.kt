package com.example.guru2_15

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MyScheWeek : AppCompatActivity(),View.OnClickListener {

    lateinit var day1Tv : TextView
    lateinit var day2Tv : TextView
    lateinit var day3Tv : TextView
    lateinit var day4Tv : TextView
    lateinit var day5Tv : TextView
    lateinit var day6Tv : TextView
    lateinit var day7Tv : TextView
    lateinit var day1Edt : EditText
    lateinit var day2Edt : EditText
    lateinit var day3Edt : EditText
    lateinit var day4Edt : EditText
    lateinit var day5Edt : EditText
    lateinit var day6Edt : EditText
    lateinit var day7Edt : EditText

    lateinit var monthBtn : Button
    lateinit var weekBtn : Button
    lateinit var dayBtn : Button
    lateinit var addScheFab : FloatingActionButton

    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase

    lateinit var getUID:String
    lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sche_week)

        monthBtn = findViewById(R.id.monthBtn)
        weekBtn = findViewById(R.id.weekBtn)
        dayBtn = findViewById(R.id.dayBtn)
        addScheFab = findViewById(R.id.addScheFab)

        monthBtn.setOnClickListener(this)
        weekBtn.setOnClickListener(this)
        dayBtn.setOnClickListener(this)
        addScheFab.setOnClickListener(this)

        dbManager = DBManager(this, "schedule", null, 1)
        sqlitedb = dbManager.readableDatabase

        if (intent.hasExtra("date")) { //일정 등록한 날짜 정보 가져오기
            date = intent.getStringExtra("date").toString()
        }
        if (intent.hasExtra("UID")) { //로그인되어있는사용자UID
            getUID = intent.getStringExtra("UID").toString()
        }

        var sName : String? = null
        var sShour:String? = null
        var sSMinute:String? = null

        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM schedule WHERE sDate = '"+date+"';",null)
        while (cursor.moveToNext()){
            sName = cursor.getString(cursor.getColumnIndexOrThrow("Sname")).toString()
            sShour = cursor.getString(cursor.getColumnIndexOrThrow("SShour")).toString()
            sSMinute = cursor.getString(cursor.getColumnIndexOrThrow("SSminute")).toString()
        }
        day1Tv.text = date
        day1Edt.setText("일정="+sName+"시간 = "+sShour+":"+sSMinute)

        sqlitedb.close()
        dbManager.close()
    }
    override fun onClick(view: View?){
        if(view!=null){
            when(view.id){
                R.id.monthBtn -> {
                    var intent = Intent(this, MyScheMonth::class.java)
                    intent.putExtra("date",date)
                    intent.putExtra("UID",getUID)
                    startActivity(intent)
                }
                R.id.weekBtn -> {

                }
                R.id.dayBtn -> {
                    var intent = Intent(this, MyScheDay::class.java)
                    intent.putExtra("date",date)
                    intent.putExtra("UID",getUID)
                    startActivity(intent)
                }
                R.id.addScheFab -> {
                    var intent = Intent(this, MainActivity2::class.java)
                    intent.putExtra("UID",getUID)
                    startActivity(intent)
                }
            }
        }
    }

}