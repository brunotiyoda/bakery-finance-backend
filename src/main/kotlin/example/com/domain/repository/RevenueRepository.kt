package example.com.domain.repository

import example.com.domain.model.Revenue
import example.com.presentation.route.responses.RevenueSummaryByRegistrarResponse
import kotlinx.datetime.LocalDate
import java.math.BigDecimal

interface RevenueRepository {

    suspend fun create(revenue: Revenue)
    suspend fun getAllRevenues(): List<Revenue>
    suspend fun getSumOfAllRevenuesByDate(date: LocalDate): BigDecimal
    suspend fun getSumOfRevenuesByRegistrarAndDate(date: LocalDate): RevenueSummaryByRegistrarResponse
    suspend fun getSumOfAllRevenuesByPeriod(startDate: LocalDate, endDate: LocalDate): BigDecimal

}