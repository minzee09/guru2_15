package com.example.guru2_15

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class FriendAdapter(private val friendList: ArrayList<Friend>) :
    RecyclerView.Adapter<FriendAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
        ): FriendAdapter.MyViewHolder {
            var itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.friend_list_item, parent, false)
            return MyViewHolder(itemView).apply {
                itemView.setOnClickListener { //프로필 클릭시 이벤트
                    val curPos: Int = absoluteAdapterPosition //위치 갖고오기 (몇번째 프로필인지 확인)
                    val friend: Friend = friendList.get(curPos)
                    Toast.makeText(parent.context, "이름 :  ${friend.name}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        override fun onBindViewHolder(holder: FriendAdapter.MyViewHolder, position: Int) {
            val friend : Friend = friendList[position]
            holder.name.text=friend.name
            holder.nickname.text=friend.nickname
        }

        override fun getItemCount() : Int {
                return friendList.size
        }

        public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val name: TextView = itemView.findViewById(R.id.name_textView)
            val nickname: TextView = itemView.findViewById(R.id.nickname_textView)
        }



}