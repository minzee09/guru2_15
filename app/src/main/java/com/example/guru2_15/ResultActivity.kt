package com.example.guru2_15

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
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
    private var mAuth: FirebaseAuth? = null
    lateinit var getUID:String
    var myStHour:String = "00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setTitle("약속 잡기")

        dbManager = DBManager(this, "schedule", null, 1)
        sqlitedb = dbManager.readableDatabase

        mAuth = FirebaseAuth.getInstance();
        getUID = mAuth!!.currentUser?.uid.toString()

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


        dbManager = DBManager(this, "schedule", null, 1)
        sqlitedb = dbManager.readableDatabase
        mAuth = FirebaseAuth.getInstance();
        getUID = mAuth!!.currentUser?.uid.toString()

        var date_ = "${stYear}년 ${stMon}월 ${stDay}일" //로그인된유저 일정 가져오기
        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM schedule WHERE UID = '"+getUID+"' AND Sdate = '"+date_+"';",null)
        while(cursor.moveToNext()) { //커서로 로그인되어있는 유저의 스케줄 정보를 가져오기
            myStHour = cursor.getString(cursor.getColumnIndexOrThrow("SShour"))
        }
        cursor.close()
        dbManager.close()

        btnRe = findViewById(R.id.btnRe)
        btnRe.setOnClickListener {
            finish()
        }
    }

}