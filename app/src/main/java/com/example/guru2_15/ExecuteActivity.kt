package com.example.guru2_15

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import java.util.*

class ExecuteActivity : AppCompatActivity() {

    var selectYear : Int = 0
    var selectMonth : Int = 0
    var selectDay : Int = 0

    var selectYear2 : Int = 0
    var selectMonth2 : Int = 0
    var selectDay2 : Int = 0

    var stHour : Int = 0
    var stMinute : Int = 0

    var fHour : Int = 0
    var fMinute : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_execute)
        setTitle("약속 잡기")

        lateinit var btnmake: Button
        lateinit var calendarView: CalendarView
        lateinit var calendarView2: CalendarView


        val stHour = findViewById<Spinner>(R.id.stHour)
        val stMinute = findViewById<Spinner>(R.id.stMinute)

        val fHour = findViewById<Spinner>(R.id.fHour)
        val fMinute = findViewById<Spinner>(R.id.fMinute)

        stHour.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.hour_list,
            android.R.layout.simple_spinner_dropdown_item
        )
        stMinute.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.minute_list,
            android.R.layout.simple_spinner_dropdown_item
        )

        fHour.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.hour_list,
            android.R.layout.simple_spinner_dropdown_item
        )
        fMinute.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.minute_list,
            android.R.layout.simple_spinner_dropdown_item
        )

        btnmake = findViewById(R.id.btnmake)
        calendarView = findViewById(R.id.calenderView)
        calendarView2 = findViewById(R.id.calenderView2)


        calendarView.setOnDateChangeListener { view, i, i2, i3 ->
            this.selectYear = i
            this.selectMonth = i2 + 1
            this.selectDay = i3

        }

        calendarView2.setOnDateChangeListener { view, i, i2, i3 ->
            this.selectYear2 = i
            this.selectMonth2 = i2 + 1
            this.selectDay2 = i3
        }

        btnmake.setOnClickListener {
            if (selectYear == 0) {
                Toast.makeText(this, "시작 날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (selectYear2 == 0) {
                Toast.makeText(this, "종료 날짜를 선택해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ResultActivity::class.java)

                intent.putExtra("stYear", Integer.toString(selectYear))
                intent.putExtra("stMon", Integer.toString(selectMonth))
                intent.putExtra("stDay", Integer.toString(selectDay))

                intent.putExtra("fYear", Integer.toString(selectYear2))
                intent.putExtra("fMon", Integer.toString(selectMonth2))
                intent.putExtra("fDay", Integer.toString(selectDay2))

                intent.putExtra("stHour", stHour.selectedItem.toString())
                intent.putExtra("stMinute", stMinute.selectedItem.toString())

                intent.putExtra("fHour", fHour.selectedItem.toString())
                intent.putExtra("fMinute", fMinute.selectedItem.toString())

//            Log.d("intentday", selectMonth.toString())
//            Log.d("intentday", selectDay.toString())

                startActivity(intent)
            }
        }

        val friendArrayList = arrayListOf<Friend>()
        val friendAdapter = FriendPickAdapter(friendArrayList)
        val db = FirebaseFirestore.getInstance()

        var recycleView = findViewById<RecyclerView>(R.id.friendPick_RV)
        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycleView.adapter = friendAdapter

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
    }
}