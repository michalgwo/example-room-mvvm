package com.example.example_room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.example_room.db.User
import com.example.example_room.db.UserRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserRepository) : ViewModel() {
    private var isUpdating = false
    private var updatingUser: User? = null

    var users = repo.users

    var nameInput = MutableLiveData<String>()
    var emailInput = MutableLiveData<String>()

    var addOrUpdateButtonText = MutableLiveData<String>()
    var clearOrDeleteButtonText = MutableLiveData<String>()

    private var statusMessage = MutableLiveData<Event<Int>>()

    val message: LiveData<Event<Int>>
        get() = statusMessage

    init {
        addOrUpdateButtonText.value = "Add"
        clearOrDeleteButtonText.value = "Clear"
    }

    fun addOrUpdate() {
        if (isUpdating) {
            val user = updatingUser ?: return
            user.name = nameInput.value ?: return
            user.email = emailInput.value ?: return
            update(user)
            finishUpdate()
            statusMessage.value = Event(R.string.user_updated)
        } else {
            val name = nameInput.value ?: return
            val email = emailInput.value ?: return

            insert(User(0, name, email))

            nameInput.value = ""
            emailInput.value = ""
            statusMessage.value = Event(R.string.user_inserted)
        }
    }

    fun clearOrDelete() {
        if (isUpdating) {
            val user = updatingUser ?: return
            delete(user)
            finishUpdate()
            statusMessage.value = Event(R.string.user_deleted)
        } else {
            deleteAll()
            statusMessage.value = Event(R.string.all_users_deleted)
        }
    }

    private fun insert(user: User) = viewModelScope.launch(IO) {
        repo.insert(user)
    }

    private fun update(user: User) = viewModelScope.launch(IO) {
        repo.update(user)
    }

    private fun delete(user: User) = viewModelScope.launch(IO) {
        repo.delete(user)
    }

    private fun deleteAll() = viewModelScope.launch(IO) {
        repo.deleteAll()
    }

    fun initUpdate(user: User) {
        isUpdating = true
        updatingUser = user
        addOrUpdateButtonText.value = "Update"
        clearOrDeleteButtonText.value = "Delete"
        nameInput.value = user.name
        emailInput.value = user.email
    }

    private fun finishUpdate() {
        isUpdating = false
        updatingUser = null
        addOrUpdateButtonText.value = "Add"
        clearOrDeleteButtonText.value = "Clear"
        nameInput.value = ""
        emailInput.value = ""
    }
}