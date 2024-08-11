package example.com.domain.service

import example.com.domain.model.Revenue
import example.com.presentation.route.requests.RevenueRequest
import example.com.presentation.route.responses.RevenueSummaryByRegistrarResponse
import kotlinx.datetime.LocalDate
import java.math.BigDecimal

interface RevenueService {
    suspend fun createRevenue(request: RevenueRequest, username: String)
    suspend fun getAllRevenues(): List<Revenue>
    suspend fun getSumOfAllRevenuesByDate(date: String): BigDecimal
    suspend fun getSumOfRevenuesByRegistrarAndDate(date: String): RevenueSummaryByRegistrarResponse
    suspend fun getSumOfAllRevenuesByPeriod(startDate: String, endDate: String): BigDecimal
    suspend fun getSumOfAllRevenuesByDateRange(startDate: LocalDate, endDate: LocalDate): BigDecimal
}