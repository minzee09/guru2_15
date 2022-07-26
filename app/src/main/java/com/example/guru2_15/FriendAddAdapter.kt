package com.example.guru2_15

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import android.widget.Filter.FilterResults
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ThreadLocalRandom

class FriendAddAdapter(var friendList: ArrayList<Friend>, var con: Context) :
    RecyclerView.Adapter<FriendAddAdapter.ViewHolder>(), Filterable {
    var TAG = "FriendAddAdapter"
    var db: FirebaseFirestore? = null
    var filteredFriendList = ArrayList<Friend>()
    var itemFilter = ItemFilter()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var email: TextView
        lateinit var profile : ImageView

        init {
            name = itemView.findViewById(R.id.name_textView)
            email = itemView.findViewById(R.id.email_textView)
            profile = itemView.findViewById(R.id.profileimageView)

            //다이어로그 발생 이벤트
            itemView.setOnClickListener {
                AlertDialog.Builder(con).apply {
                    var position = absoluteAdapterPosition
                    var friend = filteredFriendList[position]

                    setMessage(friend.name+" 친구 추가가 되었습니다.")

                    setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        //Toast.makeText(con, "OK Button Click", Toast.LENGTH_SHORT).show()
                    })
                    show()
                }
            }
        }
    }

    init {
        filteredFriendList.addAll(friendList)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FriendAddAdapter.ViewHolder {
        val con = parent.context
        val inflater = con.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate((R.layout.friend_add_item), parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend: Friend = filteredFriendList[position]
        holder.name.text = friend.name
        holder.email.text = friend.email
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
        return filteredFriendList.size
    }

    //--filter
    override fun getFilter(): Filter {
        return itemFilter
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()
            Log.d(TAG, "charSequence : $charSequence")

            //검색이 필요없을 경우를 위해 원본 배열을 복제
            val filteredList: ArrayList<Friend> = ArrayList<Friend>()
            //공백제외 아무런 값이 없을 경우 -> 원본 배열
            if (filterString.trim { it <= ' ' }.isEmpty()) {
                results.values = friendList
                results.count = friendList.size

                return results
                //공백제외 2글자 이인 경우 -> 이름으로만 검색
            } else {
                for (friend in friendList) {
                    if (friend.name.contains(filterString) || friend.email.contains(filterString)) {
                        filteredList.add(friend)
                    }
                }
            }
            results.values = filteredList
            results.count = filteredList.size

            return results
        }

        //리사이클러뷰 갱신
        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
            filteredFriendList.clear()
            filteredFriendList.addAll(filterResults.values as ArrayList<Friend>)
            notifyDataSetChanged()
        }

    }

}