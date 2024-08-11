package example.com.presentation.route

import example.com.presentation.route.requests.ExpenseRequestWrapper
import example.com.presentation.route.responses.ExpenseResponse
import example.com.application.service.ExpenseServiceImpl
import example.com.domain.service.ExpenseService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject
import example.com.utils.authenticatedAdmin
import example.com.utils.authenticatedUser

fun Application.expenseRouting() {

    val service by inject<ExpenseService>()

    routing {
        route("/expenses") {
            authenticate("auth-jwt") {
                get {
                    authenticatedAdmin {
                        val expenses = service.getAllExpenses()
                        val response = expenses.map { expense ->
                            ExpenseResponse(
                                expense.id,
                                expense.date,
                                expense.value.toDouble(),
                                expense.description,
                                expense.registeredBy
                            )
                        }
                        call.respond(response)
                    }
                }

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