package com.example.example_room

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.example_room.adapters.RecyclerViewAdapter
import com.example.example_room.databinding.ActivityMainBinding
import com.example.example_room.db.User
import com.example.example_room.db.UserDatabase
import com.example.example_room.db.UserRepository
import com.example.example_room.db.UserViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = UserDatabase.getInstance(application).userDAO
        val repo = UserRepository(dao)
        val factory = UserViewModelFactory(repo)

        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        binding.user = viewModel
        binding.lifecycleOwner = this
        initRecyclerView()

        viewModel.message.observe(this) {
            it.getContentIfNotHandled().let { msg ->
                if (msg == null)
                    return@let

                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvDatabase.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter { selectedUser: User -> itemClickListener(selectedUser) }
        binding.rvDatabase.adapter = adapter

        displayUserList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayUserList() {
        viewModel.users.observe(this) {
            adapter.updateList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun itemClickListener(user: User) {
        //Toast.makeText(this, user.name, Toast.LENGTH_SHORT).show()
        viewModel.initUpdate(user)
    }
}