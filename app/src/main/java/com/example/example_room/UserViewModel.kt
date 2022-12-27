package com.example.example_room

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.example_room.db.User
import com.example.example_room.db.UserRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val repo: UserRepository) : ViewModel() {
    private var isUpdating = false
    private var updatingUser: User? = null

    var users = repo.users

    var nameInput = MutableLiveData<String>()
    var emailInput = MutableLiveData<String>()

    var addOrUpdateButtonText = MutableLiveData<Int>()
    var clearOrDeleteButtonText = MutableLiveData<Int>()

    private var statusMessage = MutableLiveData<Event<Int>>()

    val message: LiveData<Event<Int>>
        get() = statusMessage

    init {
        addOrUpdateButtonText.value = R.string.add
        clearOrDeleteButtonText.value = R.string.clear
    }

    fun addOrUpdate() {
        if (nameInput.value.isNullOrBlank() || emailInput.value.isNullOrBlank()) {
            statusMessage.value = Event(R.string.error_fill_out_the_form)
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput.value.toString()).matches()) {
            statusMessage.value = Event(R.string.error_incorrect_email)
            return
        }

        if (isUpdating) {
            val user = updatingUser ?: return
            user.name = nameInput.value ?: return
            user.email = emailInput.value ?: return
            update(user)
        } else {
            val name = nameInput.value ?: return
            val email = emailInput.value ?: return
            insert(User(0, name, email))
        }
    }

    fun clearOrDelete() {
        if (isUpdating) {
            val user = updatingUser ?: return
            delete(user)
        } else {
            deleteAll()
        }
    }

    private fun insert(user: User) = viewModelScope.launch(IO) {
        val userId = repo.insert(user)
        withContext(Main) {
            if (userId > -1) {
                nameInput.value = ""
                emailInput.value = ""
                statusMessage.value = Event(R.string.user_inserted)
            } else {
                statusMessage.value = Event(R.string.error_occurred)
            }
        }
    }

    private fun update(user: User) = viewModelScope.launch(IO) {
        val numberOfRows = repo.update(user)
        withContext(Main) {
            if (numberOfRows > 0) {
                finishUpdate()
                statusMessage.value = Event(R.string.user_updated)
            } else {
                statusMessage.value = Event(R.string.error_occurred)
            }
        }
    }

    private fun delete(user: User) = viewModelScope.launch(IO) {
        val numberOfRows = repo.delete(user)
        withContext(Main) {
            if (numberOfRows > 0) {
                finishUpdate()
                statusMessage.value = Event(R.string.user_deleted)
            } else {
                statusMessage.value = Event(R.string.error_occurred)
            }
        }
    }

    private fun deleteAll() = viewModelScope.launch(IO) {
        val numberOfRows = repo.deleteAll()
        withContext(Main) {
            if (numberOfRows > 0) {
                statusMessage.value = Event(R.string.all_users_deleted)
            } else {
                statusMessage.value = Event(R.string.error_occurred)
            }
        }
    }

    fun initUpdate(user: User) {
        isUpdating = true
        updatingUser = user
        addOrUpdateButtonText.value = R.string.update
        clearOrDeleteButtonText.value = R.string.delete
        nameInput.value = user.name
        emailInput.value = user.email
    }

    private fun finishUpdate() {
        isUpdating = false
        updatingUser = null
        addOrUpdateButtonText.value = R.string.add
        clearOrDeleteButtonText.value = R.string.clear
        nameInput.value = ""
        emailInput.value = ""
    }
}