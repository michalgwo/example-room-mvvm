package com.example.example_room.db

class UserRepository(private val dao: UserDAO) {
    val users = dao.getAll()

    suspend fun insert(user: User): Long {
        return dao.insert(user)
    }

    suspend fun update(user: User): Int {
        return dao.update(user)
    }

    suspend fun delete(user: User): Int {
        return dao.delete(user)
    }

    suspend fun deleteAll(): Int {
        return dao.deleteAll()
    }
}