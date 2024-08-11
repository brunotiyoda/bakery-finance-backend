package example.com.utils

import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import io.ktor.http.*
import io.ktor.server.auth.principal
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.authenticatedAdmin(block: suspend () -> Unit) {
    val principal = call.principal<JWTPrincipal>()
    val userRole = principal?.payload?.getClaim("role")?.asString()

    if (userRole == "admin") {
        block()
    } else {
        call.respond(HttpStatusCode.Forbidden, "Only admin can perform this action")
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.authenticatedUser(block: suspend (String) -> Unit) {
    val principal = call.principal<JWTPrincipal>()
    val userRole = principal?.payload?.getClaim("role")?.asString()
    val username = principal?.payload?.getClaim("username")?.asString()

    if (userRole in listOf("admin", "matriz", "alvorada", "vila") && username != null) {
        block(username)
    } else {
        call.respond(HttpStatusCode.Forbidden, "Unauthorized")
    }
}