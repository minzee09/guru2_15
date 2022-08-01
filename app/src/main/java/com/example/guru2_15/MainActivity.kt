package com.example.guru2_15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.view.View
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout : DrawerLayout
    lateinit var navigationView :NavigationView

    lateinit var addScheBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addScheBtn = findViewById(R.id.addScheBtn)

        addScheBtn.setOnClickListener {
            var intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        //text view 가져오기
        var userNameTv: TextView = findViewById(R.id.userNameTv)
        var naviName: TextView
        var naviEmail: TextView


        // 상단 툴바 설정
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu)

        drawerLayout = findViewById(R.id.drawerLayout)

        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)

        val headerView = navigationView.getHeaderView(0)
        naviName = headerView.findViewById(R.id.naviNameTextView)
        naviEmail = headerView.findViewById(R.id.naviEmailTextView)

        val user = FirebaseAuth.getInstance().currentUser

        //현재 유저가 널값이라면 (로그인이 안되어있을 떄)
        if (user == null) {
            startLoginActivity()
        }
        //사용자 정보 가져오기
        user?.let {

            var name = user.uid //사용자 ID값
            //naviEmail.text=user.email //텍스트뷰에 사용자 정보 구현
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
                        email =(document["email"]as? String).toString()
                        userNameTv.text = name
                        naviName.text=name
                        naviEmail.text = email
                    }
                }
                .addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.w("MainActivity", "Error getting documents: $exception")
                }
        }
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