package com.example.guru2_15

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class ResultActivity : AppCompatActivity(){

    private val friendArrayList = arrayListOf<Friend>()
    private val friendAdapter = FriendAdapter(friendArrayList)
    private val scheMon = BooleanArray(15)
    private val scheTue = BooleanArray(15)
    private val schewed = BooleanArray(15)
    private val schethu = BooleanArray(15)
    private val schesat = BooleanArray(15)
    private val schesun = BooleanArray(15)



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

        lateinit var selectEmail : TextView

        lateinit var dbManager: DBManager
        lateinit var sqlitedb : SQLiteDatabase

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

        val friendSchedule = arrayListOf<Schedule>()
        val friendArrayList = arrayListOf<Friend>()
        val friendAdapter = FriendPickAdapter(friendArrayList, friendSchedule)

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

        for(i in 0..14){
            val scheMon = BooleanArray(15){i-> false}
            scheMon.forEach { print("$it") }
        }

        btnRe = findViewById(R.id.btnRe)

        btnRe.setOnClickListener {
            finish()
        }
    }

}