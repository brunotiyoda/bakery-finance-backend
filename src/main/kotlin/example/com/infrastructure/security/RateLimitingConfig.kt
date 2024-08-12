package example.com.infrastructure.security

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import kotlin.time.Duration.Companion.seconds

fun Application.configureRateLimiting() {
    install(RateLimit) {
        global {
            rateLimiter(limit = 100, refillPeriod = 60.seconds)
        }
        register(RateLimitName("auth")) {
            rateLimiter(limit = 10, refillPeriod = 60.seconds)
        }
    }
}