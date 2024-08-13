package example.com

import example.com.infrastructure.config.DatabaseFactory
import example.com.infrastructure.config.configureRouting
import example.com.infrastructure.config.koin
import example.com.infrastructure.config.serialization
import example.com.infrastructure.security.authenticationConfig
import example.com.infrastructure.security.configureRateLimiting
import example.com.infrastructure.security.corsConfig
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain.main

fun main(args: Array<String>) {
    main(args)
}

fun Application.module() {
    DatabaseFactory.init()

    koin()
    serialization()

    corsConfig()
    configureRateLimiting()
    authenticationConfig()

    configureRouting()
}
