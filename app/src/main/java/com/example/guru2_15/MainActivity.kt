package com.example.guru2_15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = FirebaseAuth.getInstance().currentUser

        //현재 유저가 널값이라면 (로그인이 안되어있을 떄)
        if (user == null) {
            startLoginActivity()
        } else {
            //회원가입 or 로그인
            user.let {
                for (profile in it.providerData) {
                    val name = profile.displayName
                    if (name != null) {
                        if (name.isEmpty()) {
                            startActivity(Intent(this, MemberInitActivity::class.java))
                        }
                    }
                }
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
}