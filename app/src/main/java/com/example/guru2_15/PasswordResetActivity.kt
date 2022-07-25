package com.example.guru2_15

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class PasswordResetActivity : AppCompatActivity(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset) //로그인

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        val resetpwBtn: Button = findViewById(R.id.resetpwButton)

        resetpwBtn.setOnClickListener(this)
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
                R.id.resetpwButton -> send()
            }
        }
    }

    //비밀번호 재설정
    private fun send() {
        var email: String = findViewById<EditText>(R.id.emailEditText).text.toString()

        // 이메일, 비번, 비번 확인 다 입력시(공백X) 실행
        if (email.isNotEmpty()) {
            mAuth?.sendPasswordResetEmail(email)
                ?.addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        startToast("이메일을 보냈습니다.")
                    }
                }
        } else {
            startToast("이메일 입력해주세요.")
        }
    }

    //토스트 호출 함수
    private fun startToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

