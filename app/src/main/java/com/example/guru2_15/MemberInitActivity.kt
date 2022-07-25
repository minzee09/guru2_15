package com.example.guru2_15

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest


class MemberInitActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_init) //회원정보

        val checkBtn: Button = findViewById(R.id.checkButton)

        checkBtn.setOnClickListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(1)
    }

    //클릭 이벤트 재정의
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.checkButton -> profileUpdate()
            }
        }
    }

    //기존 사용자 로그인
    private fun profileUpdate() {
        var name: String = findViewById<EditText>(R.id.nameEditText).text.toString()

        // 이메일, 비번, 비번 확인 다 입력시(공백X) 실행
        if (name.isNotEmpty()){
            val user = FirebaseAuth.getInstance().currentUser

            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            if (user != null) {
                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startToast("회원정보 등록에 성공하였습니다.")
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }
            } else {
                startToast("회원정보를 입력해주세요.")
            }
        }
    }

    //토스트 호출 함수
    private fun startToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}

