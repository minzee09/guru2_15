package com.example.guru2_15

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null
    private var mDatabaseRef : DatabaseReference? = null //실시간데베

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) //로그인

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference()

        val loginBtn: Button = findViewById(R.id.loginButton)
        val goToSignUpBtn: Button = findViewById(R.id.gotoSignUpButton)
        val goToPWResetBtn: Button = findViewById(R.id.gotoPasswordResetbutton)

        loginBtn.setOnClickListener(this)
        goToSignUpBtn.setOnClickListener(this)
        goToPWResetBtn.setOnClickListener(this)
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
                R.id.loginButton -> login()
                R.id.gotoSignUpButton -> startActivity(Intent(this, SignUpActivity::class.java))
                R.id.gotoPasswordResetbutton -> startActivity(Intent(this, PasswordResetActivity::class.java))
            }
        }
    }

    //기존 사용자 로그인
    private fun login() {
        var email: String = findViewById<EditText>(R.id.emailEditText).text.toString()
        var password: String = findViewById<EditText>(R.id.passwordEditText).text.toString()

        // 이메일, 비번, 비번 확인 다 입력시(공백X) 실행
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        startToast("로그인에 성공하였습니다.")
                        val intent = Intent(this, MyScheFirst::class.java)
                        startActivity(intent) //액티비티 전환 메소드

                        val user = mAuth!!.currentUser
                        finish()
                    } else {
                        startToast(task.exception.toString())
                    }
                }
        } else {
            startToast("이메일 또는 비밀번호를 입력해주세요.")
        }
    }

    //토스트 호출 함수
    private fun startToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    //액티비티 이동
    private fun StartMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        //뒤로 가기 방지
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //실행할 activity가 이미 스택에 존재하면 해당 activity 위에 존재하는 다른 activity 모두 종료시킨다.

        startActivity(intent) //액티비티 전환 메소드
    }
}

