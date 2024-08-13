package example.com.infrastructure.security

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

fun Application.corsConfig() {

    val allowedHosts = System.getenv("ALLOWED_HOSTS")?.split(",") ?: listOf("localhost:4200")

    install(CORS) {
        allowedHosts.forEach { hosts ->
            allowHost(hosts)
        }
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
    }
}
