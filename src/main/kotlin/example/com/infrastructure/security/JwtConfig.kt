package example.com.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import example.com.domain.model.User
import java.util.*

object JwtConfig {
    private const val secret = "your-secret-key"
    private const val issuer = "http://0.0.0.0:8080/"
    private const val audience = "http://0.0.0.0:8080/hello"
    private val algorithm = Algorithm.HMAC512(secret)
    private const val validityInMs = 36_000_00 * 10 // 10 hours

    fun generateToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withAudience(audience)
        .withClaim("username", user.username)
        .withClaim("role", user.role)
        .withExpiresAt(Date(System.currentTimeMillis() + validityInMs))
        .sign(algorithm)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

}