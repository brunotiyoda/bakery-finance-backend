package example.com.presentation.route

import example.com.application.service.AuthenticationService
import example.com.presentation.route.requests.LoginRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class AuthenticationRoutingTest {

    private val authService = mockk<AuthenticationService>()

    @Test
    fun `should fail login with invalid credentials`() = testApplication {

        coEvery {
            authService.authenticate(
                "wrongUser",
                "wrongPassword"
            )
        } returns AuthenticationService.Result.Failure

        val response = client.post("/api/login") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(LoginRequest(username = "wrongUser", password = "wrongPassword")))
        }

        println("Response status: ${response.status}")
        println("Response body: ${response.bodyAsText()}")

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        assertEquals("Invalid username or password", response.bodyAsText())
    }
}