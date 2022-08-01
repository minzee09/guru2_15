package com.example.guru2_15

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MyScheWeek : AppCompatActivity(),View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout : DrawerLayout
    lateinit var navigationView :NavigationView

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
    lateinit var year : String
    lateinit var month: String
    lateinit var day : String
    lateinit var monthBtn : Button
    lateinit var weekBtn : Button
    lateinit var dayBtn : Button
    lateinit var addScheFab : FloatingActionButton

    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase
    private var mAuth: FirebaseAuth? = null

    lateinit var getUID:String
    var sName : String? = "\uD83D\uDE45일정 없음 "
    var sShour:String? = "00"
    var sSMinute:String? = "00"
    var sEhour: String ?= "00"
    var sEMinute: String ?= "00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sche_week)

        monthBtn = findViewById(R.id.monthBtn)
        weekBtn = findViewById(R.id.weekBtn)
        dayBtn = findViewById(R.id.dayBtn)
        addScheFab = findViewById(R.id.addScheFab)
        day1Tv = findViewById(R.id.day1Tv)
        day1Edt = findViewById(R.id.day1Edt)
        day2Tv = findViewById(R.id.day2Tv)
        day2Edt = findViewById(R.id.day2Edt)
        day3Tv = findViewById(R.id.day3Tv)
        day3Edt = findViewById(R.id.day3Edt)
        day4Tv = findViewById(R.id.day4Tv)
        day4Edt = findViewById(R.id.day4Edt)
        day5Tv = findViewById(R.id.day5Tv)
        day5Edt = findViewById(R.id.day5Edt)
        day6Tv = findViewById(R.id.day6Tv)
        day6Edt = findViewById(R.id.day6Edt)
        day7Tv = findViewById(R.id.day7Tv)
        day7Edt = findViewById(R.id.day7Edt)

        monthBtn.setOnClickListener(this)
        weekBtn.setOnClickListener(this)
        dayBtn.setOnClickListener(this)
        addScheFab.setOnClickListener(this)

        dbManager = DBManager(this, "schedule", null, 1)
        sqlitedb = dbManager.readableDatabase

        mAuth = FirebaseAuth.getInstance();
        getUID = mAuth!!.currentUser?.uid.toString()


        val cal = Calendar.getInstance()//오늘날짜가져와서
        setTexts(cal)
        day1Tv.text = "${month} / ${day}"
        day1Edt.setText(""+sName+" | \uD83D\uDD52 = "+sShour+":"+sSMinute+" ~ "+sEhour+":"+sEMinute)
        cal.add(Calendar.DATE,1) //다음날로이동
        resetTexts()
        setTexts(cal)
        day2Tv.text = "${month} / ${day}"
        day2Edt.setText(""+sName+" | \uD83D\uDD52 = "+sShour+":"+sSMinute+" ~ "+sEhour+":"+sEMinute)
        cal.add(Calendar.DATE,1) //다음날로이동
        resetTexts()
        setTexts(cal)
        day3Tv.text = "${month} / ${day}"
        day3Edt.setText(""+sName+" | \uD83D\uDD52 = "+sShour+":"+sSMinute+" ~ "+sEhour+":"+sEMinute)
        cal.add(Calendar.DATE,1) //다음날로이동
        resetTexts()
        setTexts(cal)
        day4Tv.text = "${month} / ${day}"
        day4Edt.setText(""+sName+" | \uD83D\uDD52 = "+sShour+":"+sSMinute+" ~ "+sEhour+":"+sEMinute)
        cal.add(Calendar.DATE,1) //다음날로이동
        resetTexts()
        setTexts(cal)
        day5Tv.text = "${month} / ${day}"
        day5Edt.setText(""+sName+" | \uD83D\uDD52 = "+sShour+":"+sSMinute+" ~ "+sEhour+":"+sEMinute)
        cal.add(Calendar.DATE,1) //다음날로이동
        resetTexts()
        setTexts(cal)
        day6Tv.text = "${month} / ${day}"
        day6Edt.setText(""+sName+" | \uD83D\uDD52 = "+sShour+":"+sSMinute+" ~ "+sEhour+":"+sEMinute)
        cal.add(Calendar.DATE,1) //다음날로이동
        resetTexts()
        setTexts(cal)
        day7Tv.text = "${month} / ${day}"
        day7Edt.setText(""+sName+" | \uD83D\uDD52 = "+sShour+":"+sSMinute+" ~ "+sEhour+":"+sEMinute)

        sqlitedb.close()
        dbManager.close()

        // 상단 툴바 설정
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu)

        drawerLayout = findViewById(R.id.drawerLayout)

        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)


        var userNameTv: TextView = findViewById(R.id.userNameTv)
        var naviName: TextView
        var naviEmail: TextView

        val headerView = navigationView.getHeaderView(0)
        naviName = headerView.findViewById(R.id.naviNameTextView)
        naviEmail = headerView.findViewById(R.id.naviEmailTextView)


        //화면에 사용자 이름 구현
        val user = FirebaseAuth.getInstance().currentUser

        //사용자 정보 가져오기
        user?.let {

            //text view 가져오기
            var userNameTv: TextView = findViewById(R.id.userNameTv)
            var name = user.uid //사용자 ID값
            var email = user.email

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
                        naviName.text=name
                        naviEmail.text = email
                    }
                }
                .addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.w("MyScheWeek", "Error getting documents: $exception")
                }
        }

    }
    fun setTexts(cal : Calendar){
        year = cal.get(Calendar.YEAR).toString()
        month = (cal.get(Calendar.MONTH) + 1).toString()
        day = cal.get(Calendar.DATE).toString()
        var date_ = "${year}년 ${month}월 ${day}일"
        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM schedule WHERE UID = '"+getUID+"' AND Sdate = '"+date_+"';",null)
        while(cursor.moveToNext()) {
            sName="\uD83D\uDE46"+cursor.getString(cursor.getColumnIndexOrThrow("Sname"))
            sShour=cursor.getString(cursor.getColumnIndexOrThrow("SShour"))
            sSMinute=cursor.getString(cursor.getColumnIndexOrThrow("SSminute"))
            sEhour=cursor.getString(cursor.getColumnIndexOrThrow("SEhour"))
            sEMinute=cursor.getString(cursor.getColumnIndexOrThrow("SEminute"))
        }
    }
    fun resetTexts(){
        sName = "\uD83D\uDE45일정 없음"
        sShour = "00"
        sSMinute="00"
    }
    override fun onClick(view: View?){
        if(view!=null){
            when(view.id){
                R.id.monthBtn -> {
                    var intent = Intent(this, MyScheMonth::class.java)
                    startActivity(intent)
                }
                R.id.weekBtn -> {
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

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0) //액티비티 전환 애니메이션 제거

    }

    //로그인 액티비티로 이동
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
                val intent = Intent(this, MyScheDay::class.java)
                startActivity(intent)
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