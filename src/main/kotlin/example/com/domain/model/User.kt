package example.com.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val username: String,
    val password: String,
    val role: String
)
