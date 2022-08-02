package com.example.guru2_15

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ThreadLocalRandom

class FriendPickAdapter(val friendList: ArrayList<Friend>) :
    RecyclerView.Adapter<FriendPickAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FriendPickAdapter.ViewHolder {

        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.friend_pick_item, parent, false)
        return ViewHolder(view).apply {
            view.setOnClickListener { //프로필 클릭시 이벤트
                val curPos: Int = absoluteAdapterPosition //위치 갖고오기 (몇번째 프로필인지 확인)
                val friend: Friend = friendList.get(curPos)
                Toast.makeText(parent.context, "${friend.name} 을 선택함", Toast.LENGTH_SHORT)
                    .show()

                db.collection("userInfo")
                    .whereEqualTo("name",friend.name)
                    .get()
                    .addOnSuccessListener { move ->
                        for(document in move){
                            val item = Friend(document["name"] as String, document["email"] as String)

                            friendList.add(item)

//                            val intent: Intent = Intent(this@FriendPickAdapter,ResultActivity::class.java)
//                            intent.putExtra("email",friend.email)
                        }

                    }

            }
        }
    }

    override fun onBindViewHolder(holder: FriendPickAdapter.ViewHolder, position: Int) {
        holder.name.text = friendList[position].name
        //랜덤함수를 사용하여 프로필 사진이 무작위하게 적용
        val random = ThreadLocalRandom.current().nextInt(1, 5)
        when (random) {
            1 -> holder.profile.setImageResource(R.drawable.profile)
            2 -> holder.profile.setImageResource(R.drawable.profile1)
            3 -> holder.profile.setImageResource(R.drawable.profile2)
            4 -> holder.profile.setImageResource(R.drawable.profile3)
        }
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name_textView)
        val profile: ImageView = itemView.findViewById(R.id.profileimageView)
    }

}