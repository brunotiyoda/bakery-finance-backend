package example.com.infrastructure.config

import example.com.di.appModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin

fun Application.koin() {
    install(Koin) {
        modules(
            appModule
        )
    }
}