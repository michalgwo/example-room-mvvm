package com.example.example_room

import android.os.Bundle
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
    }

    private fun initRecyclerView() {
        binding.rvDatabase.layoutManager = LinearLayoutManager(this)
        displayUserList()
    }

    private fun displayUserList() {
        viewModel.users.observe(this) {
            binding.rvDatabase.adapter = RecyclerViewAdapter(it) { selectedUser: User ->
                itemClickListener(selectedUser)
            }
        }
    }

    private fun itemClickListener(user: User) {
        //Toast.makeText(this, user.name, Toast.LENGTH_SHORT).show()
        viewModel.initUpdate(user)
    }
}