package example.com.domain.repository

import example.com.domain.model.User

interface UserRepository {
    suspend fun findByUsername(username: String): User?
}