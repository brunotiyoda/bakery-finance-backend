package example.com.presentation.route

import example.com.domain.service.ExpenseService
import example.com.presentation.route.requests.ExpenseRequestWrapper
import example.com.utils.authenticatedUser
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Routing.expenseRouting() {

    val service by inject<ExpenseService>()

    route("/api") {
        route("/expenses") {
            authenticate("auth-jwt") {
                post {
                    authenticatedUser { username ->
                        val requests = call.receive<ExpenseRequestWrapper>()
                        service.createExpenses(requests, username)
                        call.respond(HttpStatusCode.Created, requests)
                    }
                }
            }
        }
    }
}