package com.example.guru2_15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MyScheFirst : AppCompatActivity() {

    lateinit var addScheBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sche_first)

        addScheBtn = findViewById(R.id.addScheBtn)

        addScheBtn.setOnClickListener {
            var intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)

        }
    }
}