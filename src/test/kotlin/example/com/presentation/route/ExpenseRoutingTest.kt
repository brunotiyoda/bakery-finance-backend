package example.com.presentation.route

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import example.com.presentation.route.requests.ExpenseRequest
import example.com.presentation.route.requests.ExpenseRequestWrapper
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpenseRoutingTest {

    private fun createTestToken(): String {
        val secret = System.getenv("JWT_SECRET") ?: throw IllegalStateException("JWT_SECRET não configurado")
        val issuer = System.getenv("JWT_ISSUER") ?: "http://0.0.0.0:8080/"
        val algorithm = Algorithm.HMAC512(secret)

        return JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withClaim("username", "testUser")
            .withClaim("role", "admin")
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(algorithm)
    }

    @Test
    fun `should create expense`() = testApplication {
        val token = createTestToken()

        val response = client.post("/api/expenses") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(Json.encodeToString(ExpenseRequestWrapper(listOf(ExpenseRequest(100.0, "Despesa de teste")))))
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `should get expenses by date`() = testApplication {
        val token = createTestToken()

        val response = client.get("/api/reports/expenses/2023-08-01") {
            bearerAuth(token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}