package example.com.application.service

import example.com.infrastructure.security.PasswordEncryption
import example.com.domain.model.User
import example.com.domain.repository.UserRepository

class AuthenticationService(
    private val userRepository: UserRepository
) {

    sealed class Result {
        data class Success(val user: User) : Result()
        object Failure : Result()
    }

    suspend fun authenticate(username: String, password: String): Result {
        val user = userRepository.findByUsername(username)
        return if (user != null && PasswordEncryption.verifyPassword(password, user.password)) {
            Result.Success(user)
        } else {
            Result.Failure
        }
    }
}