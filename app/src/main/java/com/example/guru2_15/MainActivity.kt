package com.example.guru2_15

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 상단 툴바 설정
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu)

        drawerLayout = findViewById(R.id.drawerLayout)

        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)


        var auth : FirebaseAuth? = null
        auth = Firebase.auth

        val user = FirebaseAuth.getInstance().currentUser

        //현재 유저가 널값이라면 (로그인이 안되어있을 떄)
        if (user == null) {
            startLoginActivity()
        } else {
            var firestore : FirebaseFirestore? = null
            firestore = FirebaseFirestore.getInstance()
            val docRef = firestore.collection("userInfo").document((auth!!.currentUser!!.uid))
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        if(document.data == null){
                            startActivity(Intent(this, MemberInitActivity::class.java))
                        }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }

        val logoutBtn: Button = findViewById(R.id.logoutButton)
        val friendBtn: Button = findViewById(R.id.gotoFriendButton)

        logoutBtn.setOnClickListener(this)
        friendBtn.setOnClickListener(this)
    }

    //클릭 이벤트 재정의
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.logoutButton -> {
                    FirebaseAuth.getInstance().signOut()
                    startLoginActivity()
                }
                R.id.gotoFriendButton -> startActivity(Intent(this, FriendListActivity::class.java))
            }
        }
    }

    //로그인 액티비티로 이동
    private fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item!!.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> startActivity(Intent(this, MainActivity::class.java))
            R.id.make -> startActivity(Intent(this, ExecuteActivity::class.java))
            R.id.friend -> startActivity(Intent(this, FriendListActivity::class.java))
            R.id.app_info -> {
                val intent = Intent(this, Info::class.java)
                startActivity(intent)
                return true
            }
        }
        return false
    }

}