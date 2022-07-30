package com.example.guru2_15

import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MyScheMonth : AppCompatActivity(),View.OnClickListener,NavigationView.OnNavigationItemSelectedListener  {

    lateinit var drawerLayout : DrawerLayout
    lateinit var navigationView : NavigationView

    lateinit var calendarView: CalendarView
    lateinit var addScheFab : FloatingActionButton
    lateinit var monthBtn : Button
    lateinit var weekBtn : Button
    lateinit var dayBtn : Button

    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase
    private var mAuth: FirebaseAuth? = null

    lateinit var getUID:String
    lateinit var date: String
    var sName: String ?= "일정 없음"
    var sShour: String ?= "00"
    var sSMinute: String ?= "00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sche_month)

        // 상단 툴바 설정ㅁ
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu)

        drawerLayout = findViewById(R.id.drawerLayout)

        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)

        // UI값 생성
        calendarView = findViewById(R.id.calendarView)
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
        mAuth = FirebaseAuth.getInstance();
        getUID = mAuth!!.currentUser?.uid.toString()

        if (intent.hasExtra("date")) { //일정 등록한 날짜 정보 가져오기
            date = intent.getStringExtra("date").toString()
        }

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var cursor: Cursor
            var date_ = "${year}년 ${month+1}월 ${dayOfMonth}일"

            var builder = AlertDialog.Builder(this)
            builder.setTitle("일정!!")
            var v1 = layoutInflater.inflate(R.layout.activity_sche_dialog, null)
            builder.setView(v1)
            sName= "일정 없음"
            sShour= "00"
            sSMinute= "00"
            cursor = sqlitedb.rawQuery("SELECT * FROM schedule WHERE UID = '"+getUID+"' AND Sdate = '"+date_+"';",null)
            while(cursor.moveToNext()) {
                sName=cursor.getString(cursor.getColumnIndexOrThrow("Sname"))
                sShour=cursor.getString(cursor.getColumnIndexOrThrow("SShour"))
                sSMinute=cursor.getString(cursor.getColumnIndexOrThrow("SSminute"))
            }
            builder.setMessage("${sName} | 시간 = ${sShour}: ${sSMinute}")
            builder.show()
        }
    }
    override fun onClick(view: View?){
        if(view!=null){
            when(view.id){
                R.id.monthBtn -> {
                }
                R.id.weekBtn -> {
                    var intent = Intent(this, MyScheWeek::class.java)
                    intent.putExtra("date",date)
                    startActivity(intent)
                }
                R.id.dayBtn -> {
                    var intent = Intent(this, MyScheDay::class.java)
                    intent.putExtra("date",date)
                    startActivity(intent)
                }
                R.id.addScheFab -> {
                    var intent = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item!!.itemId){
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                drawerLayout.closeDrawers()
                return true
            }
            R.id.make -> {
                val intent = Intent(this, ExecuteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.friend -> {
                val intent = Intent(this, FriendListActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.app_info -> {
                val intent = Intent(this, Info::class.java)
                startActivity(intent)
                return true
            }
            R.id.app_logout -> {
                FirebaseAuth.getInstance().signOut()
                startLoginActivity()
            }
        }
        return false
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers()
        }else{
            super.onBackPressed()
        }
    }


}