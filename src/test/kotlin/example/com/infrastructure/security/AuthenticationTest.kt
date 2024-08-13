package example.com.infrastructure.security

import com.auth0.jwt.JWT
import example.com.application.service.AuthenticationService
import example.com.domain.model.User
import example.com.domain.repository.UserRepository
import example.com.infrastructure.TestDatabaseFactory
import example.com.module
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthenticationTest {
    private lateinit var userRepository: UserRepository
    private lateinit var authenticationService: AuthenticationService

    @BeforeTest
    fun setup() {
        TestDatabaseFactory.init()
        userRepository = mockk()
        authenticationService = AuthenticationService(userRepository)
    }

    @Ignore
    fun `should authenticate valid user`(): Unit = withTestApplication({ module() }) {
        val validUser = User(1, "validuser", PasswordEncryption.hashPassword("validpassword"), "USER")
        coEvery { userRepository.findByUsername("validuser") } returns validUser

        handleRequest(HttpMethod.Post, "/api/login") {
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(mapOf("username" to "validuser", "password" to "validpassword")))
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            val responseBody = response.content
            assertNotNull(responseBody)
            if (responseBody != null) {
                assertTrue(responseBody.contains("token"))
            }

            // Verificar se o token é válido
            val token = responseBody?.let { Json.decodeFromString<Map<String, String>>(it) }?.get("token")
            assertNotNull(token)
            val decodedJWT = JWT.decode(token)
            assertEquals("validuser", decodedJWT.getClaim("username").asString())
        }
    }

    @Test
    fun `should reject invalid user`(): Unit = withTestApplication({ module() }) {
        coEvery { userRepository.findByUsername("invaliduser") } returns null

        handleRequest(HttpMethod.Post, "/api/login") {
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(mapOf("username" to "invaliduser", "password" to "invalidpassword")))
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, response.status())
        }
    }

    @Test
    fun `should register new user`(): Unit = withTestApplication({ module() }) {
        coEvery { userRepository.create(any()) } returns User(1, "newuser", "hashedpassword", "USER")

        handleRequest(HttpMethod.Post, "/api/register") {
            addHeader("Content-Type", "application/json")
            setBody(Json.encodeToString(mapOf("username" to "newuser", "password" to "newpassword", "role" to "USER")))
        }.apply {
            assertEquals(HttpStatusCode.Created, response.status())
            assertEquals("User registered successfully", response.content)
        }
    }

    @Test
    fun `should verify password correctly`() {
        val password = "mypassword"
        val hashedPassword = PasswordEncryption.hashPassword(password)
        assertTrue(PasswordEncryption.verifyPassword(password, hashedPassword))
    }

    @Test
    fun `should not verify incorrect password`() {
        val password = "mypassword"
        val hashedPassword = PasswordEncryption.hashPassword(password)
        assertTrue(!PasswordEncryption.verifyPassword("wrongpassword", hashedPassword))
    }

    @Test
    fun `should generate valid JWT token`() {
        val user = User(1, "testuser", "hashedpassword", "USER")
        val token = JwtConfig.generateToken(user)
        val decodedJWT = JWT.decode(token)

        assertEquals("testuser", decodedJWT.getClaim("username").asString())
        assertEquals("USER", decodedJWT.getClaim("role").asString())
        assertNotNull(decodedJWT.expiresAt)
    }
}