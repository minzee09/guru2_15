package com.example.guru2_15

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.nvt.color.ColorPickerDialog
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity2 : AppCompatActivity() {

    var selectedDate: Date? = null

    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase
    lateinit var EdtSchedulName : EditText // 일정이름
    lateinit var Colorr : Button  //색
    lateinit var BtnColorSelect : Button  // 색 선택 버튼
    lateinit var BtnDate : Button  //날짜 선택 버튼
    lateinit var SpinnerStartHour : Spinner  //일정 시작하는 시간 시(0시~23시)
    lateinit var SpinnerStartMinute : Spinner //일정 시작하는 시간 분 (0분, 30분)
    lateinit var SpinnerEndHour : Spinner  //일정 끝나는 시간 시간 시(0시~23시)
    lateinit var SpinnerEndMinute : Spinner //일정 끝나는 시간 분 (0분, 30분)
    lateinit var EdtPlace : EditText  //일정 장소
    lateinit var EdtMemo : EditText  //일정 메모
    lateinit var BtnInsert : Button  //일정 등록버튼

    private var mAuth: FirebaseAuth? = null
    //유저
    private var user: FirebaseUser?= null


    lateinit var getUID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        mAuth = FirebaseAuth.getInstance();
        getUID = mAuth!!.currentUser?.uid.toString()

        dbManager = DBManager(this, "schedule", null, 1)

        EdtSchedulName = findViewById(R.id.EdtScheduleName)
        Colorr = findViewById(R.id.Color)
        BtnColorSelect = findViewById(R.id.BtnColorSelect)
        BtnDate = findViewById(R.id.BtnDate)
        SpinnerStartHour = findViewById(R.id.SpinnerStartHour)
        SpinnerStartMinute = findViewById(R.id.SpinnerStartMinute)
        SpinnerEndHour = findViewById(R.id.SpinnerEndHour)
        SpinnerEndMinute = findViewById(R.id.SpinnerEndMinute)
        EdtPlace = findViewById(R.id.EdtPlace)
        EdtMemo = findViewById(R.id.EdtMemo)
        BtnInsert = findViewById(R.id.BtnInsert)


        //일정이름을 입력하면 등록하기 버튼 활성화
        EdtSchedulName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.length > 0) {
                    BtnInsert.setClickable(true)
                    BtnInsert.isEnabled = true
                    BtnInsert.setBackgroundColor(resources.getColor(R.color.main3))
                    BtnInsert.setTextColor(resources.getColor(R.color.white))
                } else {
                    BtnInsert.setClickable(false)
                    BtnInsert.isEnabled = false
                }
            }
        })

        //일정 색 설정하기 (colorPicker)
        BtnColorSelect.setOnClickListener {
            val colorPicker = ColorPickerDialog(
                this,
                Color.BLACK, // color init
                true, // true is show alpha
                object : ColorPickerDialog.OnColorPickerListener {
                    override fun onCancel(dialog: ColorPickerDialog?) {
                        // handle click button Cancel
                    }

                    override fun onOk(dialog: ColorPickerDialog?, colorPicker: Int) {
                        // handle click button OK
                        Colorr.setBackgroundColor(colorPicker)
                    }
                })
            colorPicker.show()
        }


        //일정 날짜 선택하기
        BtnDate.setOnClickListener {
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    BtnDate.setText("${year}년 ${month + 1}월 ${dayOfMonth}일")
                }
            }, year, month, date)
            dlg.show()
        }

        val Adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.hour_list,
            android.R.layout.simple_spinner_dropdown_item
        )
        val Adapter2 = ArrayAdapter.createFromResource(
            this,
            R.array.minute_list,
            android.R.layout.simple_spinner_dropdown_item
        )

        SpinnerStartHour.setAdapter(Adapter1)
        SpinnerStartMinute.setAdapter(Adapter2)
        SpinnerEndHour.setAdapter(Adapter1)
        SpinnerEndMinute.setAdapter(Adapter2)

        //입력한 일정 데이터베이스로 전달
        BtnInsert.setOnClickListener {
            //일정 파이어베이스에 등록
            scheUpdate()

            var str_sname : String = EdtSchedulName.text.toString()
            var str_color: String = Colorr.background.toString()
            var str_date: String = BtnDate.text.toString()
            var str_startHour: String = SpinnerStartHour.selectedItem.toString()
            var str_startMinute: String = SpinnerStartMinute.selectedItem.toString()
            var str_endHour: String = SpinnerEndHour.selectedItem.toString()
            var str_endMinute: String = SpinnerEndMinute.selectedItem.toString()
            var str_splce: String = EdtPlace.text.toString()
            var str_smemo: String = EdtMemo.text.toString()


            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO schedule VALUES ('" + str_sname + "' , '" + str_color + "', '"
                    + str_date + "', '"+ str_startHour + "' ,'" + str_startMinute + "', '"
                    + str_endHour + "' , '" + str_endMinute + "', '"+ str_splce+ "', '" + str_smemo + "', '" + getUID + "');")
            sqlitedb.close()
            dbManager.close()

            val intent = Intent(this, MyScheDay::class.java)
            intent.putExtra("date",str_date)
            Toast.makeText(applicationContext, "입력됨 ${str_color}", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }


    }

    fun scheUpdate(){
        var schename : String = EdtSchedulName.text.toString()
        var schecolor : String = Colorr.background.toString()
        var schedate: String = BtnDate.text.toString()
        var scheshour : String = SpinnerStartHour.selectedItem.toString()
        var schesminute : String = SpinnerStartMinute.selectedItem.toString()
        var scheehour : String = SpinnerEndHour.selectedItem.toString()
        var scheemiute: String = SpinnerEndMinute.selectedItem.toString()
        var scheplace: String = EdtPlace.text.toString()
        var schememo: String = EdtMemo.text.toString()

        if(schename.length > 0 && schedate.toString() != "날짜 선택"){
            user = FirebaseAuth.getInstance().getCurrentUser()
            val scheInfo : ScheInfo = ScheInfo(schename, schecolor, schedate,
                scheshour, schesminute, scheehour, scheemiute, scheplace, schememo, getUID)

            uploader(scheInfo);
        }else{
            Toast.makeText(applicationContext, "일정이름 또는 날짜가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun uploader(scheinfo: ScheInfo){
        val db = FirebaseFirestore.getInstance()
        db.collection("sche")
            .add(scheinfo)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    inner class myDBHelper(context: Context) : SQLiteOpenHelper(context, "scheduleDB", null, 1){
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL("CREATE TABLE schedule ( Sname text, Scolor text, Sdate text, SShour text, SSminute text, SEhour text, SEminute text, Splace text, Smemo text);")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS groupTBL")
        }
    }
}
