package com.example.guru2_15

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2_15.databinding.ActivityFriendAddBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*

class FriendAddActivity : AppCompatActivity() {
    val TAG = "FriendAddActivity"

    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityFriendAddBinding? = null
    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    var friendArrayList =arrayListOf<Friend>()
    private var friendAddAdapter =FriendAddAdapter(friendArrayList,this)

    private val db = FirebaseFirestore.getInstance()


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFriendAddBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.friendRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.friendRecyclerView.adapter = friendAddAdapter

        binding.searchViewFriend.setOnQueryTextListener(searchViewTextListener)

        db.collection("userInfo")   // 작업할 컬렉션
            .get()      // 문서 가져오기
            .addOnSuccessListener { result ->
                // 성공할 경우
                friendArrayList.clear()
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    val item = Friend(document["name"] as String, document["email"] as String)
                    friendArrayList.add(item)
                }
                friendAddAdapter.notifyDataSetChanged()  // 리사이클러 뷰 갱신
            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                Log.w("FriendAddActivity", "Error getting documents: $exception")
            }

        binding.friendListButton.setOnClickListener {
            startActivity(Intent(this, FriendListActivity::class.java))
        }
        binding.friendAddButton.setOnClickListener {
            startActivity(Intent(this, FriendAddActivity::class.java))
        }

    }
    //SearchView 텍스트 입력시 이벤트
    var searchViewTextListener: SearchView.OnQueryTextListener=
        object :SearchView.OnQueryTextListener {
            //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            //텍스트 입력 or 수정시에 호출
            override fun onQueryTextChange(s: String?): Boolean {
                friendAddAdapter.filter.filter(s)
                Log.d(TAG, "SearchVies Text is changed : $s")
                return false
            }
        }

    override fun onDestroy() {
        // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        super.onDestroy()
    }

    override fun onPause() {
        //액티비티 전환 애니메이션 제가
        super.onPause()
        overridePendingTransition(0, 0) //액티비티 전환 애니메이션 제가

    }
}

