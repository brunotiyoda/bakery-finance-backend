package example.com.presentation.route

import example.com.infrastructure.security.PasswordEncryption
import example.com.domain.model.User
import example.com.infrastructure.persistence.UserRepositoryImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.userRouting() {

    val userRepositoryImpl = UserRepositoryImpl()

    routing {
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

            get("/all") {
                val principal = call.principal<JWTPrincipal>()
                val userRole = principal?.payload?.getClaim("role")?.asString()

                when (userRole) {
                    "admin", "user" -> {
                        // Implementar lógica para buscar todos os usuários
                        call.respond(HttpStatusCode.OK, "List of all users")
                    }

                    else -> {
                        call.respond(HttpStatusCode.Forbidden, "Only admin or users can view all users")
                    }
                }
            }
        }
    }
}