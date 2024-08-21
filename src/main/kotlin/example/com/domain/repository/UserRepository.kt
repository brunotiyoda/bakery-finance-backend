package example.com.domain.repository

import example.com.domain.model.User

interface UserRepository {
    suspend fun create(user: User): User?
    suspend fun findByUsername(username: String): User?
}