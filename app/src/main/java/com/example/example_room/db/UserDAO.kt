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
    suspend fun insert(user: User): Long

    @Update
    suspend fun update(user: User): Int

    @Delete
    suspend fun delete(user: User): Int

    @Query("DELETE FROM user_data_table")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM user_data_table")
    fun getAll(): LiveData<List<User>>

}