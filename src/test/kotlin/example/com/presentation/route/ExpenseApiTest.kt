package example.com.presentation.route

import example.com.module
import example.com.presentation.route.requests.ExpenseRequest
import example.com.presentation.route.requests.ExpenseRequestWrapper
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Ignore
import kotlin.test.assertEquals

class ExpenseApiTest {
    @Ignore
    fun `should create expense`() = withTestApplication({ module() }) {
        with(handleRequest(HttpMethod.Post, "/api/expenses") {
            addHeader("Content-Type", "application/json")
            addHeader("Authorization", "Bearer valid_token")
            setBody(Json.encodeToString(ExpenseRequestWrapper(listOf(ExpenseRequest(100.0, "Test expense")))))
        }) {
            assertEquals(HttpStatusCode.Created, response.status())
        }
    }

    @Ignore
    fun `should get expenses by date`() = withTestApplication({ module() }) {
        with(handleRequest(HttpMethod.Get, "/api/reports/expenses/2023-08-01") {
            addHeader("Authorization", "Bearer valid_admin_token")
        }) {
            assertEquals(HttpStatusCode.OK, response.status())
            // Add more assertions to verify the response body
        }
    }
}