package com.example.guru2_15

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null
    private var mDatabaseRef : DatabaseReference? = null //실시간데베

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup) //회원가입

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("guru2_15")

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
        var passwordCheck: String = findViewById<EditText>(R.id.passwordCheckEditText).text.toString()

        // 이메일, 비번, 비번 확인 다 입력시(공백X) 실행
        if(email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty()) {
            // 비번하고 비번 확인이 같을때 회원가입 실행
            if (password.equals(passwordCheck)) {
                mAuth!!.createUserWithEmailAndPassword(email, password) //파이어베이스유저등록
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) { //회원가입이 됐을 때
                            var firebaseUser : FirebaseUser?=null
                            firebaseUser = mAuth!!.currentUser
                            val user = mAuth!!.currentUser
                            var account = UserAccount("","","")
                            account.email = user?.email.toString()
                            account.password = password
                            account.idToken = user!!.uid

                            //setValue : database에 insert하기
                            mDatabaseRef!!.child("UserAccount").child(user.uid).setValue(account)

                            startActivity(Intent(this,MemberInitActivity::class.java))
                        } else {
                            if (task.exception != null) {
                                startToast(task.exception.toString())
                            }
                        }
                    }
            } else {
                startToast("비밍번호가 일치하지 않습니다.\n다시 입력해주세요.")
            }
        } else {
            startToast("이메일 또는 비밀번호를 입력해주세요.")
        }
    }

    //토스트 호출 함수
    private fun startToast(msg : String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
