package example.com.presentation.route

import example.com.domain.service.RevenueService
import example.com.presentation.route.requests.RevenueRequest
import example.com.presentation.route.responses.RevenueResponse
import example.com.utils.authenticatedAdmin
import example.com.utils.authenticatedUser
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Routing.revenueRouting() {

    val service by inject<RevenueService>()

    route("/api") {
        route("/revenues") {
            authenticate("auth-jwt") {
                get {
                    authenticatedAdmin {
                        val revenues = service.getAllRevenues()
                        val response = revenues.map { revenue ->
                            RevenueResponse(
                                revenue.id,
                                revenue.date,
                                revenue.money.toDouble(),
                                revenue.card.toDouble(),
                                revenue.pix.toDouble(),
                                revenue.voucher.toDouble(),
                                revenue.registeredBy
                            )
                        }
                        call.respond(response)
                    }
                }

                post {
                    authenticatedUser { username ->
                        val request = call.receive<RevenueRequest>()
                        service.createRevenue(request, username)
                        call.respond(HttpStatusCode.Created, request)
                    }
                }
            }
        }
    }
}