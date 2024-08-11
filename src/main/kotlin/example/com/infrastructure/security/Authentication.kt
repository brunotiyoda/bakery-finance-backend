package example.com.infrastructure.security

import example.com.infrastructure.persistence.UserRepositoryImpl
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.authenticationConfig() {
    val userRepositoryImpl = UserRepositoryImpl()

    install(Authentication) {
        jwt("auth-jwt") {
            realm = "Access to 'hello'"
            verifier(JwtConfig.verifier)
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }

        basic("auth-basic") {
            realm = "Access to the '/' path"
            validate { credentials ->
                val user = userRepositoryImpl.findByUsername(credentials.name)
                if (user != null && PasswordEncryption.verifyPassword(credentials.password, user.password)) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}