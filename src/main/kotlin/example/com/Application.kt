package example.com

import example.com.infrastructure.config.DatabaseFactory
import example.com.infrastructure.security.authenticationConfig
import example.com.infrastructure.security.corsConfig
import example.com.infrastructure.config.koin
import example.com.infrastructure.config.serialization
import example.com.presentation.route.authenticationRouting
import example.com.presentation.route.expenseRouting
import example.com.presentation.route.revenueRouting
import example.com.presentation.route.reportRouting
import example.com.presentation.route.userRouting
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain.main

fun main(args: Array<String>) {
    DatabaseFactory.init()
    main(args)
}

fun Application.module() {
    koin()
    corsConfig()
    authenticationConfig()
    serialization()

    userRouting()
    authenticationRouting()
    revenueRouting()
    expenseRouting()
    reportRouting()
}
