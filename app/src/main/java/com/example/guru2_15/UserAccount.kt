package com.example.guru2_15

import android.widget.EditText

//사용자 계정 정보 모델 클래스

class UserAccount(var idToken : String, var email: String,
                    var password: String) {

 /*  lateinit var idToken : String //firebase Uid 고유 토큰 정보, 사용자만 가지는 고유key값
   lateinit var email: String
   lateinit var password: String
*/
    //빈 생성자 만들기?
   constructor() : this("","","")
}