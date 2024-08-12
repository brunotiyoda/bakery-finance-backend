package example.com.presentation.route

import example.com.application.service.AuthenticationService
import example.com.infrastructure.security.JwtConfig
import example.com.presentation.route.requests.LoginRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Routing.authenticationRouting() {

    val service by inject<AuthenticationService>()

    route("/api") {
        post("/login") {
            val loginRequest = call.receive<LoginRequest>()
            val result = service.authenticate(loginRequest.username, loginRequest.password)

            when (result) {
                is AuthenticationService.Result.Success -> {
                    val token = JwtConfig.generateToken(result.user)
                    call.respond(HttpStatusCode.OK, mapOf("token" to token))
                }

                is AuthenticationService.Result.Failure -> {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid username or password")
                }
            }
        }
    }
}