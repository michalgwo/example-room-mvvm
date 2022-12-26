package com.example.example_room.db

class UserRepository(private val dao: UserDAO) {
    val users = dao.getAll()

    suspend fun insert(user: User) {
        dao.insert(user)
    }

    suspend fun update(user: User) {
        dao.update(user)
    }

    suspend fun delete(user: User) {
        dao.delete(user)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}