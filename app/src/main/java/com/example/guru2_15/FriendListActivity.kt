package com.example.guru2_15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class FriendListActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        val user = FirebaseAuth.getInstance().currentUser

        val friendRV: RecyclerView = findViewById(R.id.recycler_view)

        //data - 아직 데이터베이스 연결 X
        val friendList = arrayListOf(
            Friend(R.drawable.profile,"김이름1","email@gmail.com"),
            Friend(R.drawable.profile,"김이름2","email2@gmail.com"),
            Friend(R.drawable.profile,"김이름3","email3@gmail.com"),
            Friend(R.drawable.profile,"김이름4","email4@gmail.com"),
            Friend(R.drawable.profile,"김이름5","email5@gmail.com"),
            Friend(R.drawable.profile,"김이름6","email6@gmail.com"),
        )
        friendRV.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false) //세로 방향
        friendRV.setHasFixedSize(true) //리사이클뷰 성능 개선

        friendRV.adapter=FriendAdapter(friendList)
    }

    //클릭 이벤트 재정의
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.logoutButton -> {
                    FirebaseAuth.getInstance().signOut()
                    startLoginActivity()
                }
            }
        }
    }

    //로그인 액티비티로 이동
    private fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}