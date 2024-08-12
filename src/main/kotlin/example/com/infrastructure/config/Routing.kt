package example.com.infrastructure.config

import example.com.presentation.route.authenticationRouting
import example.com.presentation.route.expenseRouting
import example.com.presentation.route.reportRouting
import example.com.presentation.route.revenueRouting
import example.com.presentation.route.userRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        userRouting()
        authenticationRouting()
        revenueRouting()
        expenseRouting()
        reportRouting()
    }
}