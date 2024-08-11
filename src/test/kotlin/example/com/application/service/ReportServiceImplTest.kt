package example.com.application.service

import example.com.domain.model.DailyReportItem
import example.com.domain.model.PeriodReport
import example.com.domain.service.ExpenseService
import example.com.domain.service.RevenueService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class ReportServiceImplTest {

    private lateinit var reportService: ReportServiceImpl
    private val revenueService: RevenueService = mockk()
    private val expenseService: ExpenseService = mockk()

    @BeforeTest
    fun setup() {
        reportService = ReportServiceImpl(revenueService, expenseService)
    }

    @Test
    fun `getPeriodReport should return correct PeriodReport for given date range`() = runBlocking {
        // Arrange
        val startDate = LocalDate(2024, 8, 10)
        val endDate = LocalDate(2024, 8, 12)

        val dailyRevenues = listOf(
            BigDecimal("100.00"),
            BigDecimal("150.00"),
            BigDecimal("200.00")
        )

        val dailyExpenses = listOf(
            BigDecimal("50.00"),
            BigDecimal("75.00"),
            BigDecimal("100.00")
        )

        dailyRevenues.forEachIndexed { index, revenue ->
            coEvery {
                revenueService.getSumOfAllRevenuesByDate(
                    startDate.plus(DatePeriod(days = index)).toString()
                )
            } returns revenue
        }

        dailyExpenses.forEachIndexed { index, expense ->
            coEvery {
                expenseService.getSumOfAllExpensesByDate(
                    startDate.plus(DatePeriod(days = index)).toString()
                )
            } returns expense
        }

        // Act
        val result = reportService.getPeriodReport(startDate, endDate)

        // Assert
        val expectedDailyReports = listOf(
            DailyReportItem(LocalDate(2024, 8, 10), BigDecimal("100.00"), BigDecimal("50.00"), BigDecimal("50.00")),
            DailyReportItem(LocalDate(2024, 8, 11), BigDecimal("150.00"), BigDecimal("75.00"), BigDecimal("75.00")),
            DailyReportItem(LocalDate(2024, 8, 12), BigDecimal("200.00"), BigDecimal("100.00"), BigDecimal("100.00"))
        )

        val expectedPeriodReport = PeriodReport(
            startDate = startDate,
            endDate = endDate,
            dailyReports = expectedDailyReports,
            totalRevenue = BigDecimal("450.00"),
            totalExpense = BigDecimal("225.00"),
            totalNetBalance = BigDecimal("225.00")
        )

        assertEquals(expectedPeriodReport, result, "The returned PeriodReport should match the expected values")
    }

    @Test
    fun `getPeriodReport should return empty report when start date is after end date`() = runBlocking {
        // Arrange
        val startDate = LocalDate(2024, 8, 12)
        val endDate = LocalDate(2024, 8, 10)

        // Act
        val result = reportService.getPeriodReport(startDate, endDate)

        // Assert
        val expectedPeriodReport = PeriodReport(
            startDate = startDate,
            endDate = endDate,
            dailyReports = emptyList(),
            totalRevenue = BigDecimal.ZERO,
            totalExpense = BigDecimal.ZERO,
            totalNetBalance = BigDecimal.ZERO
        )

        assertEquals(
            expectedPeriodReport,
            result,
            "The returned PeriodReport should be empty when start date is after end date"
        )
    }
}