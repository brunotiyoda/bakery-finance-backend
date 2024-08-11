package example.com.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import example.com.domain.model.User
import java.util.*

object JwtConfig {
    private val secret = System.getenv("JWT_SECRET") ?: throw IllegalStateException("JWT_SECRET não configurado")
    private val issuer = System.getenv("JWT_ISSUER") ?: "http://0.0.0.0:8080/"
    private val audience = System.getenv("JWT_AUDIENCE") ?: "http://0.0.0.0:8080/hello"
    private val algorithm = Algorithm.HMAC512(secret)
    private val validityInMs = System.getenv("JWT_VALIDITY_MS")?.toLong() ?: (36_000_00) // 1 hora

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