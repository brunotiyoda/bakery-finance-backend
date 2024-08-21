package example.com.application.service

import example.com.domain.model.Money
import example.com.domain.model.Revenue
import example.com.domain.repository.RevenueRepository
import example.com.presentation.route.requests.RevenueRequest
import example.com.presentation.route.responses.RevenueSummaryByRegistrarResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.junit.Before
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class RevenueServiceImplTest {

    private lateinit var repository: RevenueRepository
    private lateinit var service: RevenueServiceImpl

    @Before
    fun setup() {
        repository = mockk()
        service = RevenueServiceImpl(repository)
    }

    @Test
    fun `create should call repository create with correct Revenue`() = runBlocking {

        val request = RevenueRequest(100.0, 200.0, 300.0, 400.0)
        val username = "testUser"
        coEvery { repository.create(any()) } returns Unit

        service.create(request, username)

        coVerify { repository.create(match {
            it.money == Money(BigDecimal("100.0")) &&
                    it.card == Money(BigDecimal("200.0")) &&
                    it.pix == Money(BigDecimal("300.0")) &&
                    it.voucher == Money(BigDecimal("400.0")) &&
                    it.registeredBy == username
        }) }
    }

    @Test
    fun `getAllRevenues should return list from repository`() = runBlocking {

        val expectedRevenues = listOf(mockk<Revenue>(), mockk<Revenue>())
        coEvery { repository.getAllRevenues() } returns expectedRevenues

        val result = service.getAllRevenues()

        assertEquals(expectedRevenues, result)
    }

    @Test
    fun `getSumOfAllRevenuesByDate should return Money from repository`() = runBlocking {

        val date = "2023-08-21"
        val expectedSum = Money(BigDecimal("1000.0"))
        coEvery { repository.getSumOfAllRevenuesByDate(any()) } returns expectedSum

        val result = service.getSumOfAllRevenuesByDate(date)

        assertEquals(expectedSum, result)
        coVerify { repository.getSumOfAllRevenuesByDate(LocalDate.parse(date)) }
    }

    @Test
    fun `getSumOfRevenuesByRegistrarAndDate should return response from repository`() = runBlocking {

        val date = "2023-08-21"
        val expectedResponse = mockk<RevenueSummaryByRegistrarResponse>()
        coEvery { repository.getSumOfRevenuesByRegistrarAndDate(any()) } returns expectedResponse

        val result = service.getSumOfRevenuesByRegistrarAndDate(date)

        assertEquals(expectedResponse, result)
        coVerify { repository.getSumOfRevenuesByRegistrarAndDate(LocalDate.parse(date)) }
    }

    @Test
    fun `getSumOfAllRevenuesByPeriod should return Money from repository`() = runBlocking {

        val startDate = "2023-08-01"
        val endDate = "2023-08-31"
        val expectedSum = Money(BigDecimal("5000.0"))
        coEvery { repository.getSumOfAllRevenuesByPeriod(any(), any()) } returns expectedSum

        val result = service.getSumOfAllRevenuesByPeriod(startDate, endDate)

        assertEquals(expectedSum, result)
        coVerify { repository.getSumOfAllRevenuesByPeriod(LocalDate.parse(startDate), LocalDate.parse(endDate)) }
    }
}