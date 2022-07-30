package com.example.guru2_15

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MyScheDay : AppCompatActivity (),View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout : DrawerLayout
    lateinit var navigationView : NavigationView

    lateinit var monthBtn : Button
    lateinit var weekBtn : Button
    lateinit var dayBtn : Button
    lateinit var addScheFab : FloatingActionButton
    lateinit var scheInfoTv:TextView
    lateinit var pieChart: PieChart
    var arrayColor = arrayListOf<Int>(Color.BLUE,Color.GRAY,Color.GREEN)

    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase

    lateinit var getUID:String
    lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sche_day)

        if (intent.hasExtra("date")) { //일정 등록한 날짜 정보 가져오기
            date = intent.getStringExtra("date").toString()
        }
        if (intent.hasExtra("UID")) { //로그인되어있는사용자UID
            getUID = intent.getStringExtra("UID").toString()
        }


        monthBtn = findViewById(R.id.monthBtn)
        weekBtn = findViewById(R.id.weekBtn)
        dayBtn = findViewById(R.id.dayBtn)
        addScheFab = findViewById(R.id.addScheFab)
        scheInfoTv = findViewById(R.id.scheInfoTv)

        monthBtn.setOnClickListener(this)
        weekBtn.setOnClickListener(this)
        dayBtn.setOnClickListener(this)
        addScheFab.setOnClickListener(this)

        pieChart = findViewById(R.id.chart)
        var pieDataSet = PieDataSet(data1(),"하루 일정")
        pieDataSet.setColors(arrayColor)
        pieChart.setDrawEntryLabels(false) //차트에글자표시여부
        pieChart.setUsePercentValues(false)//퍼센트표시여부
        pieChart.isDrawHoleEnabled = false
        pieChart.transparentCircleRadius = 61f

        var pieData = PieData(pieDataSet)
        pieChart.setData(pieData)
        pieChart.invalidate()

        dbManager = DBManager(this, "schedule", null, 1)
        sqlitedb = dbManager.readableDatabase

        var sName : String? = null
        var sShour:String? = null
        var sSMinute:String? = null

        var cursor : Cursor
        //cursor = sqlitedb.rawQuery("SELECT * FROM schedule WHERE UID = '" + getUID +"';",null)
        cursor = sqlitedb.rawQuery("SELECT UID, Sdate FROM schedule WHERE UID = '"+getUID+"'",null)
        while (cursor.moveToNext()){
            sName = cursor.getString(cursor.getColumnIndexOrThrow("Sname")).toString()
            sShour = cursor.getString(cursor.getColumnIndexOrThrow("SShour")).toString()
            sSMinute = cursor.getString(cursor.getColumnIndexOrThrow("SSminute")).toString()
        }
        scheInfoTv.text = "일정 "+sName + "시간 "+sShour+":"+sSMinute


        // 상단 툴바 설정
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu)

        drawerLayout = findViewById(R.id.drawerLayout)

        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)

        sqlitedb.close()
        dbManager.close()
    }


    fun data1() : ArrayList<PieEntry>{
        var datavalue = ArrayList<PieEntry>()

        datavalue.add(PieEntry(30.0f,"전공 수업"))
        datavalue.add(PieEntry(50.0f,"과제"))
        datavalue.add(PieEntry(20.0f,"운동"))

        return datavalue
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
                    var intent = Intent(this, MyScheWeek::class.java)
                    intent.putExtra("date",date)
                    intent.putExtra("UID",getUID)
                    startActivity(intent)
                }
                R.id.dayBtn -> {
                }
                R.id.addScheFab -> {
                    var intent = Intent(this, MainActivity2::class.java)
                    intent.putExtra("UID",getUID)
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