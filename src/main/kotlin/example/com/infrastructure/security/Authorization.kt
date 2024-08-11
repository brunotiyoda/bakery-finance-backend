package example.com.infrastructure.security


import io.ktor.http.HttpStatusCode
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.application.install
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RouteSelector
import io.ktor.server.routing.RouteSelectorEvaluation
import io.ktor.server.routing.RoutingResolveContext

val AuthorizationPlugin = createRouteScopedPlugin(
    name = "AuthorizationPlugin",
    createConfiguration = ::PluginConfiguration
) {
    val requiredRole = pluginConfig.role
    onCall { call ->
        val principal = call.principal<JWTPrincipal>()
        val userRole = principal?.payload?.getClaim("role")?.asString()
        if (userRole != requiredRole) {
            call.respond(HttpStatusCode.Forbidden, "Access denied. Required role: $requiredRole")
            return@onCall
        }
    }
}

class PluginConfiguration {
    var role: String = ""
}

fun Route.authorize(role: String, build: Route.() -> Unit): Route {
    val authorizedRoute = createChild(AuthorizationRouteSelector(role))
    authorizedRoute.install(AuthorizationPlugin) {
        this.role = role
    }
    authorizedRoute.build()
    return authorizedRoute
}

class AuthorizationRouteSelector(val role: String) : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant
}