package com.example.guru2_15

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class MyScheDay : AppCompatActivity (),View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout : DrawerLayout
    lateinit var navigationView : NavigationView

    lateinit var monthBtn : Button
    lateinit var weekBtn : Button
    lateinit var dayBtn : Button
    lateinit var addScheFab : FloatingActionButton
    lateinit var scheInfoTv:TextView
    lateinit var pieChart: PieChart

    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase
    private var mAuth: FirebaseAuth? = null

    lateinit var getUID:String
    lateinit var date: String
    var sName : String? = null
    var sShour:String? = null
    var sSMinute:String? = null
    var scolor:String? = null
    lateinit var year : String
    lateinit var month: String
    lateinit var day : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sche_day)

        mAuth = FirebaseAuth.getInstance();
        getUID = mAuth!!.currentUser?.uid.toString()

        val cal = Calendar.getInstance()//오늘날짜가져와서
        year = cal.get(Calendar.YEAR).toString()
        month = (cal.get(Calendar.MONTH) + 1).toString()
        day = cal.get(Calendar.DATE).toString()
        var date_ = "${year}년 ${month}월 ${day}일"

        monthBtn = findViewById(R.id.monthBtn)
        weekBtn = findViewById(R.id.weekBtn)
        dayBtn = findViewById(R.id.dayBtn)
        addScheFab = findViewById(R.id.addScheFab)
        scheInfoTv = findViewById(R.id.scheInfoTv)

        monthBtn.setOnClickListener(this)
        weekBtn.setOnClickListener(this)
        dayBtn.setOnClickListener(this)
        addScheFab.setOnClickListener(this)

        dbManager = DBManager(this, "schedule", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM schedule WHERE UID = '"+getUID+"' AND Sdate = '"+date_+"';",null)
        while(cursor.moveToNext()) {
            sName=cursor.getString(cursor.getColumnIndexOrThrow("Sname"))
            sShour=cursor.getString(cursor.getColumnIndexOrThrow("SShour"))
            sSMinute=cursor.getString(cursor.getColumnIndexOrThrow("SSminute"))
            scolor=cursor.getString(cursor.getColumnIndexOrThrow("Scolor"))
        }
        scheInfoTv.text = date_ +"오늘 일정은 "+sName + " | 시간 = "+sShour+":"+sSMinute
        //if(cursor==null){scheInfoTv.text = date_ +" 오늘 일정은 없습니다!" }
        cursor.close()

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

        pieChart = findViewById(R.id.chart)
        var pieDataSet = PieDataSet(data1(),"일정")
        var arrayColor = arrayListOf<Int>(Color.GREEN,Color.YELLOW)
        pieDataSet.setColors(arrayColor)
        pieChart.setEntryLabelTextSize(30.0f)
        pieChart.setDrawEntryLabels(true) //차트에글자표시여부
        pieChart.setUsePercentValues(false)//퍼센트표시여부
        pieChart.isDrawHoleEnabled = false
        pieChart.transparentCircleRadius = 61f

        var pieData = PieData(pieDataSet)
        pieChart.setData(pieData)
        pieChart.invalidate()

        //화면에 사용자 이름 구현
        val user = FirebaseAuth.getInstance().currentUser

        //사용자 정보 가져오기
        user?.let {

            //text view 가져오기
            var userNameTv: TextView = findViewById(R.id.userNameTv)
            var name = user.uid //사용자 ID값

            val db = FirebaseFirestore.getInstance()

            //사용자 이름 화면 연결
            db.collection("userInfo").document(name)// 작업할 컬렉션 및 다큐먼트
                .get()
                .addOnSuccessListener { document ->
                    // 성공할 경우
                    if (document != null) {
                        name = (document["name"] as? String).toString()
                        //텍스트뷰에 사용자 정보 구현
                        userNameTv.text = name
                        //naviName.text=name
                    }
                }
                .addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.w("MyScheDay", "Error getting documents: $exception")
                }
        }
    }


    fun data1() : ArrayList<PieEntry>{
        var datavalue = ArrayList<PieEntry>()

        datavalue.add(PieEntry(100.0f,sName))

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

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0) //액티비티 전환 애니메이션 제거

    }

}