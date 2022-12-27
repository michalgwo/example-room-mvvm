package com.example.example_room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.example_room.db.User
import com.example.example_room.db.UserRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserRepository) : ViewModel() {
    var users = repo.users

    var nameInput = MutableLiveData<String>()
    var emailInput = MutableLiveData<String>()

    var addOrUpdateButtonText = MutableLiveData<String>()
    var clearOrDeleteButtonText = MutableLiveData<String>()

    init {
        addOrUpdateButtonText.value = "Add"
        clearOrDeleteButtonText.value = "Clear"
    }

    fun addOrUpdate() {
        val name = nameInput.value!!
        val email = emailInput.value!!

        insert(User(0, name, email))

        nameInput.value = ""
        emailInput.value = ""
    }

    fun clearOrDelete() {
        deleteAll()
    }

    fun insert(user: User) = viewModelScope.launch(IO) {
        repo.insert(user)
    }

    fun update(user: User) = viewModelScope.launch(IO) {
        repo.update(user)
    }

    fun delete(user: User) = viewModelScope.launch(IO) {
        repo.delete(user)
    }

    fun deleteAll() = viewModelScope.launch(IO) {
        repo.deleteAll()
    }
}