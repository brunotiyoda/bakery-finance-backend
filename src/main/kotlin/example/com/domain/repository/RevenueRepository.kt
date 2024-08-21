package example.com.domain.repository

import example.com.domain.model.Money
import example.com.domain.model.Revenue
import example.com.presentation.route.responses.RevenueSummaryByRegistrarResponse
import kotlinx.datetime.LocalDate

interface RevenueRepository {

    suspend fun create(revenue: Revenue)
    suspend fun getAllRevenues(): List<Revenue>
    suspend fun getSumOfAllRevenuesByDate(date: LocalDate): Money
    suspend fun getSumOfRevenuesByRegistrarAndDate(date: LocalDate): RevenueSummaryByRegistrarResponse
    suspend fun getSumOfAllRevenuesByPeriod(startDate: LocalDate, endDate: LocalDate): Money

}