package com.example.guru2_15

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase

    private var mAuth: FirebaseAuth? = null
    var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup) //회원가입

        dbManager = DBManager(this, "userInfo", null, 1)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance()

        val signUpBtn: Button = findViewById(R.id.signUpButton)

        signUpBtn.setOnClickListener(this)
    }

    //클릭 이벤트 재정의
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.signUpButton -> signUp()
            }
        }
    }

    //신규 사용자 가입
    private fun signUp() {
        var email: String = findViewById<EditText>(R.id.emailEditText).text.toString()
        var password: String = findViewById<EditText>(R.id.passwordEditText).text.toString()
        var passwordCheck: String =
            findViewById<EditText>(R.id.passwordCheckEditText).text.toString()
        var name: String = findViewById<EditText>(R.id.nameEditText).text.toString()

        // 이메일, 비번, 비번 확인 다 입력시(공백X) 실행
        if (email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty() && name.isNotEmpty()) {
            // 비번하고 비번 확인이 같을때 회원가입 실행
            if (password.equals(passwordCheck)) {
                mAuth!!.createUserWithEmailAndPassword(email, password) //파이어베이스 유저 등록
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) { //회원가입이 됐을 때
                            val user = mAuth!!.currentUser

                            sqlitedb = dbManager.writableDatabase //회원이메일,파이어베이스 uid 저장 데이터베이스
                            sqlitedb.execSQL("INSERT INTO userInfo VALUES ('"+user?.email+ "', '" +user?.uid+ "');")
                            sqlitedb.close()

                            profileUpdate()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            if (task.exception != null) {
                                startToast(task.exception.toString())
                            }
                        }
                    }
            } else {
                startToast("비밀번호가 일치하지 않습니다.\n다시 입력해주세요.")
            }
        } else {
            startToast("이메일 또는 비밀번호를 입력해주세요.")
        }
    }

    //토스트 호출 함수
    private fun startToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    //사용자 정보 데이터베이스에 추가
    private fun profileUpdate() {
        var name: String = findViewById<EditText>(R.id.nameEditText).text.toString()
        var email: String = findViewById<EditText>(R.id.emailEditText).text.toString()

        //다 입력시(공백X) 실행
        if (name.isNotEmpty() && email.isNotEmpty()) {
            //데이터 추가
            var data = Friend(name, email)
            db?.collection("userInfo")?.document(mAuth!!.currentUser!!.uid)?.set(data)
        } else {
            startToast("회원정보를 입력해주세요.")
        }
    }

}
