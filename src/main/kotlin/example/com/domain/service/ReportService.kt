package example.com.domain.service

import example.com.domain.model.DailyTotal
import example.com.domain.model.MonthlyTotal
import example.com.domain.model.PeriodReport
import example.com.presentation.route.responses.RevenueSummaryByRegistrarResponse
import kotlinx.datetime.LocalDate

interface ReportService {

    suspend fun dailyTotal(date: String): DailyTotal
    suspend fun getSumOfRevenuesByRegistrarAndDate(date: String): RevenueSummaryByRegistrarResponse
    suspend fun getPeriodReport(startDate: LocalDate, endDate: LocalDate): PeriodReport
    suspend fun monthlyTotal(month: Int, year: Int): MonthlyTotal
}