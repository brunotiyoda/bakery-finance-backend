package example.com.domain.repository

import example.com.domain.model.Money
import example.com.domain.model.Revenue
import example.com.domain.model.RevenueCategory
import example.com.presentation.route.responses.RevenueSummaryByRegistrarResponse
import kotlinx.datetime.LocalDate

interface RevenueRepository {

    suspend fun create(revenue: Revenue)
    suspend fun getAllRevenues(): List<Revenue>
    suspend fun getSumOfAllRevenuesByDate(date: LocalDate): Money
    suspend fun getSumOfRevenuesByRegistrarAndDate(date: LocalDate): Map<String, Map<RevenueCategory, Money>>
    suspend fun getSumOfAllRevenuesByPeriod(startDate: LocalDate, endDate: LocalDate): Money

}