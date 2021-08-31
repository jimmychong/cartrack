package com.cartrack.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cartrack.R
import com.cartrack.databinding.ViewholderUserBinding
import com.cartrack.model.user.UserInfo

class UserAdapter(val onUserClickListener:OnUserClickListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var users = mutableListOf<UserInfo>()

    interface OnUserClickListener{
        fun userClicked(user : UserInfo)
    }

    fun setUserList(movies: List<UserInfo>) {
        this.users = movies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return from(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(
            users[holder.absoluteAdapterPosition]
        )

        holder.itemView.setOnClickListener {
            onUserClickListener.userClicked(users[holder.absoluteAdapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }


    class UserViewHolder(private val binding: ViewholderUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var user: UserInfo? = null

        fun bind(user: UserInfo?) {
            this.user = user

            user?.let {
                binding.tvFullName.text =String.format(itemView.context.getString(R.string.user_name), user.name, user.username)
                binding.tvAddress.text = user.address?.fullAddress()
                binding.tvPhoneNumber.text = user.phone
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): UserViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ViewholderUserBinding.inflate(layoutInflater, parent, false)
            return UserViewHolder(binding)
        }
    }
}