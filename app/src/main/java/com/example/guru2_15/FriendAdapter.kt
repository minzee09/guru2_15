package com.example.guru2_15

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class FriendAdapter(val friendList: ArrayList<Friend>) :
    RecyclerView.Adapter<FriendAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendAdapter.CustomViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.friend_list_item, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener { //프로필 클릭시 이벤트
                val curPos: Int = absoluteAdapterPosition //위치 갖고오기 (몇번째 프로필인지 확인)
                val friend: Friend = friendList.get(curPos)
                Toast.makeText(parent.context,"이름 :  ${friend.name}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBindViewHolder(holder: FriendAdapter.CustomViewHolder, position: Int) {
        //오류 수정 예정
        // holder.profile.setImageResource(friendList.get(position).profile)
        holder.name.text = friendList.get(position).name
        holder.email.text = friendList.get(position).email
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile = itemView.findViewById<ImageView>(R.id.profileimageView) //프로필 이미지
        val name = itemView.findViewById<TextView>(R.id.name_textView) //이름
        val email = itemView.findViewById<TextView>(R.id.email_textView) //이메일

    }
}