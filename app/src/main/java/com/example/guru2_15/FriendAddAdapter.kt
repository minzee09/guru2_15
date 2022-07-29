package com.example.guru2_15

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class FriendAddAdapter(val friendList: ArrayList<Friend>) :
    RecyclerView.Adapter<FriendAddAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): FriendAddAdapter.ViewHolder {

        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.friend_add_item, parent, false)
        return ViewHolder(view).apply {
            view.setOnClickListener { //프로필 클릭시 이벤트
                val curPos: Int = absoluteAdapterPosition //위치 갖고오기 (몇번째 프로필인지 확인)
                val friend: Friend = friendList.get(curPos)
                Toast.makeText(parent.context, "이름 :  ${friend.name}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onBindViewHolder(holder: FriendAddAdapter.ViewHolder, position: Int) {
        holder.name.text=friendList[position].name
        holder.email.text=friendList[position].email
    }

    override fun getItemCount() : Int {
        return friendList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name_textView)
        val email: TextView = itemView.findViewById(R.id.email_textView)
    }

}