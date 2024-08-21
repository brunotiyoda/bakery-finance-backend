package example.com.application.service

import example.com.domain.model.User
import example.com.domain.repository.UserRepository
import example.com.infrastructure.security.PasswordEncryption
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AuthenticationServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var authService: AuthenticationService

    @Before
    fun setup() {
        userRepository = mockk()
        authService = AuthenticationService(userRepository)
        mockkObject(PasswordEncryption)
    }

    @Test
    fun `authenticate should return Success when credentials are valid`() = runBlocking {

        val username = "testUser"
        val password = "correctPassword"
        val hashedPassword = "hashedPassword"
        val user = User(1, username, hashedPassword, "user")

        coEvery { userRepository.findByUsername(username) } returns user
        every { PasswordEncryption.verifyPassword(password, hashedPassword) } returns true

        val result = authService.authenticate(username, password)

        assertTrue(result is AuthenticationService.Result.Success)
        assertEquals(user, (result as AuthenticationService.Result.Success).user)
    }

    @Test
    fun `authenticate should return Failure when user is not found`() = runBlocking {

        val username = "nonExistentUser"
        val password = "anyPassword"

        coEvery { userRepository.findByUsername(username) } returns null

        val result = authService.authenticate(username, password)

        assertTrue(result is AuthenticationService.Result.Failure)
    }

    @Test
    fun `authenticate should return Failure when password is incorrect`() = runBlocking {

        val username = "testUser"
        val password = "wrongPassword"
        val hashedPassword = "hashedPassword"
        val user = User(1, username, hashedPassword, "user")

        coEvery { userRepository.findByUsername(username) } returns user
        every { PasswordEncryption.verifyPassword(password, hashedPassword) } returns false

        val result = authService.authenticate(username, password)

        assertTrue(result is AuthenticationService.Result.Failure)
    }
}