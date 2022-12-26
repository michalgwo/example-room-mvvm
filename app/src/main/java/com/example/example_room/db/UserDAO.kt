package com.example.example_room.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDAO {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user_data_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM user_data_table")
    fun getAll(): LiveData<List<User>>

}