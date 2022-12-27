package com.example.example_room

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.example_room.databinding.ActivityMainBinding
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

        displayUserList()
    }

    private fun displayUserList() {
        viewModel.users.observe(this) {
            Log.d("MainActivity", it.toString())
        }
    }
}