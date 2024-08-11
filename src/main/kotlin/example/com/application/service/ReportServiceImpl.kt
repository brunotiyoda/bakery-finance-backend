package example.com.application.service

import example.com.domain.model.DailyReportItem
import example.com.domain.model.DailyTotal
import example.com.domain.model.MonthlyTotal
import example.com.domain.model.PeriodReport
import example.com.domain.service.ExpenseService
import example.com.domain.service.ReportService
import example.com.domain.service.RevenueService
import example.com.presentation.route.responses.RevenueSummaryByRegistrarResponse
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import java.math.BigDecimal

class ReportServiceImpl(
    private val revenueService: RevenueService,
    private val expenseService: ExpenseService
) : ReportService {

    override suspend fun dailyTotal(date: String): DailyTotal {
        val revenues = revenueService.getSumOfAllRevenuesByDate(date)
        val expenses = expenseService.getSumOfAllExpensesByDate(date)

        val dailyTotal = DailyTotal(
            revenues = revenues,
            expenses = expenses,
            netBalance = revenues - expenses
        )

        return dailyTotal
    }

    override suspend fun getSumOfRevenuesByRegistrarAndDate(date: String): RevenueSummaryByRegistrarResponse {
        return revenueService.getSumOfRevenuesByRegistrarAndDate(date)
    }

    override suspend fun getPeriodReport(startDate: LocalDate, endDate: LocalDate): PeriodReport {
        val dailyReports = mutableListOf<DailyReportItem>()
        var currentDate = startDate
        var totalRevenue = BigDecimal.ZERO
        var totalExpense = BigDecimal.ZERO

        while (currentDate <= endDate) {
            val dailyRevenue = revenueService.getSumOfAllRevenuesByDate(currentDate.toString())
            val dailyExpense = expenseService.getSumOfAllExpensesByDate(currentDate.toString())
            val dailyNetBalance = dailyRevenue - dailyExpense

            dailyReports.add(DailyReportItem(currentDate, dailyRevenue, dailyExpense, dailyNetBalance))

            totalRevenue += dailyRevenue
            totalExpense += dailyExpense

            currentDate = currentDate.plus(DatePeriod(days = 1))
        }

        val totalNetBalance = totalRevenue - totalExpense

        return PeriodReport(startDate, endDate, dailyReports, totalRevenue, totalExpense, totalNetBalance)
    }

    override suspend fun monthlyTotal(month: Int, year: Int): MonthlyTotal {
        val startDate = LocalDate(year, month, 1)
        val endDate = startDate.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)

        val revenues = revenueService.getSumOfAllRevenuesByPeriod(startDate.toString(), endDate.toString())
        val expenses = expenseService.getSumOfAllExpensesByPeriod(startDate.toString(), endDate.toString())

        return MonthlyTotal(
            revenues = revenues,
            expenses = expenses,
            netBalance = revenues - expenses
        )
    }
}