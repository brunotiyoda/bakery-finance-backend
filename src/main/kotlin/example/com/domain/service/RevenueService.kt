package example.com.domain.service

import example.com.domain.model.Money
import example.com.domain.model.Revenue
import example.com.domain.model.RevenueCategory
import example.com.presentation.route.requests.RevenueRequest
import example.com.presentation.route.responses.RevenueSummaryByRegistrarResponse

interface RevenueService {
    suspend fun create(requests: List<RevenueRequest>, username: String): List<Revenue>
    suspend fun getAllRevenues(): List<Revenue>
    suspend fun getSumOfAllRevenuesByDate(date: String): Money
    suspend fun getSumOfRevenuesByRegistrarAndDate(date: String): Map<String, Map<RevenueCategory, Money>>
    suspend fun getSumOfAllRevenuesByPeriod(startDate: String, endDate: String): Money
}