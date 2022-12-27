package com.example.example_room.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.example_room.R
import com.example.example_room.databinding.ListItemBinding
import com.example.example_room.db.User

class RecyclerViewAdapter(private val users: List<User>,
                          private val itemClickListener: (User)->Unit
): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding = DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, itemClickListener: (User)->Unit) {
            binding.tvName.text = user.name
            binding.tvEmail.text = user.email
            binding.cvResult.setOnClickListener{
                itemClickListener(user)
            }
        }
    }
}