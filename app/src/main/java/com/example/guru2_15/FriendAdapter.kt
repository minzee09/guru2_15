package com.example.guru2_15

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.ThreadLocalRandom

class FriendAdapter(val friendList: ArrayList<Friend>) :
    RecyclerView.Adapter<FriendAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FriendAdapter.ViewHolder {

        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.friend_list_item, parent, false)
        return ViewHolder(view).apply {
            view.setOnClickListener { //프로필 클릭시 이벤트 발생 하는 기능
                val curPos: Int = absoluteAdapterPosition //위치 갖고오기 (몇번째 프로필인지 확인)
                val friend: Friend = friendList.get(curPos)
                Toast.makeText(parent.context, "이름 :  ${friend.name}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onBindViewHolder(holder: FriendAdapter.ViewHolder, position: Int) {
        holder.name.text = friendList[position].name
        holder.email.text = friendList[position].email
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
        val email: TextView = itemView.findViewById(R.id.email_textView)
        val profile: ImageView = itemView.findViewById(R.id.profileimageView)
    }

}