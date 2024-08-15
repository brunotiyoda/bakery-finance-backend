package example.com.presentation.route

import example.com.domain.model.User
import example.com.infrastructure.persistence.UserRepositoryImpl
import example.com.infrastructure.security.PasswordEncryption
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Routing.userRouting() {

    val userRepositoryImpl = UserRepositoryImpl()

    route("/api") {
        post("/register") {
            val user = call.receive<User>()
            val hashedPassword = PasswordEncryption.hashPassword(user.password)
            val newUser = user.copy(password = hashedPassword)

            userRepositoryImpl.create(newUser)?.let {
                call.respond(HttpStatusCode.Created, "User registered successfully")
            } ?: call.respond(HttpStatusCode.BadRequest, "Failed to register user")
        }

        authenticate("auth-jwt") {
            post("/create") {
                val principal = call.principal<JWTPrincipal>()
                val userRole = principal?.payload?.getClaim("role")?.asString()

                if (userRole == "admin") {
                    val user = call.receive<User>()
                    val hashedPassword = PasswordEncryption.hashPassword(user.password)
                    val newUser = user.copy(password = hashedPassword)
                    userRepositoryImpl.create(newUser)?.let {
                        call.respond(HttpStatusCode.Created, "User created successfully")
                    } ?: call.respond(HttpStatusCode.BadRequest, "Failed to create user")
                } else {
                    call.respond(HttpStatusCode.Forbidden, "Only admin can create users")
                }
            }
        }
    }
}