package com.example.guru2_15

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class FriendListActivity : AppCompatActivity() {
    private lateinit var recycleView: RecyclerView
    private  var friendArrayList= arrayListOf<Friend>()
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        recycleView = findViewById(R.id.friend_recycler_view)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.setHasFixedSize(true)

        friendArrayList = arrayListOf()

        friendAdapter = FriendAdapter(friendArrayList)

        recycleView.adapter = FriendAdapter(friendArrayList)

        EventChangeListener()

    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("userInfo").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        friendArrayList.add(dc.document.toObject(Friend::class.java))
                    }
                }
                friendAdapter.notifyDataSetChanged()
            }
        })
    }
}