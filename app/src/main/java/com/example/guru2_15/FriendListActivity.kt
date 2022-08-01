package com.example.guru2_15

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2_15.databinding.ActivityFriendBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject


class FriendListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityFriendBinding? = null

    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    private val friendArrayList = arrayListOf<Friend>()
    private val friendAdapter = FriendAdapter(friendArrayList)
    private val db = FirebaseFirestore.getInstance()

    lateinit var drawerLayout : DrawerLayout
    lateinit var navigationView :NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFriendBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var naviName: TextView
        var naviEmail: TextView

        binding.friendRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.friendRecyclerView.adapter = friendAdapter

        db.collection("userInfo")   // 작업할 컬렉션
            .get()      // 문서 가져오기
            .addOnSuccessListener { result ->
                // 성공할 경우
                friendArrayList.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    val item = Friend(document["name"] as String, document["email"] as String)
                    friendArrayList.add(item)
                }
                friendAdapter.notifyDataSetChanged()  // 리사이클러 뷰 갱신
            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                Log.w("FriendListActivity", "Error getting documents: $exception")
            }

        binding.friendListButton.setOnClickListener {
            startActivity(Intent(this, FriendListActivity::class.java))
        }
        binding.friendAddButton.setOnClickListener {
            startActivity(Intent(this, FriendAddActivity::class.java))
        }

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

        //사용자 정보 가져오기
        user?.let {

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
                        email =(document["email"]as? String).toString()
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

    override fun onDestroy() {
        // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        super.onDestroy()
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