package example.com.application.service

import example.com.domain.model.Money
import example.com.domain.model.Revenue
import example.com.domain.repository.RevenueRepository
import example.com.domain.service.RevenueService
import example.com.presentation.route.requests.RevenueRequest
import example.com.presentation.route.responses.RevenueSummaryByRegistrarResponse
import kotlinx.datetime.LocalDate

class RevenueServiceImpl(
    private val repository: RevenueRepository
) : RevenueService {

    override suspend fun create(request: RevenueRequest, username: String) {
        val revenue = Revenue.create(request, username)
        repository.create(revenue)
    }

    override suspend fun getAllRevenues(): List<Revenue> {
        return repository.getAllRevenues()
    }

    override suspend fun getSumOfAllRevenuesByDate(date: String): Money {
        val localDate = LocalDate.parse(date)
        return repository.getSumOfAllRevenuesByDate(localDate)
    }

    override suspend fun getSumOfRevenuesByRegistrarAndDate(date: String): RevenueSummaryByRegistrarResponse {
        val localDate = LocalDate.parse(date)
        return repository.getSumOfRevenuesByRegistrarAndDate(localDate)
    }

    override suspend fun getSumOfAllRevenuesByPeriod(startDate: String, endDate: String): Money {
        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        return repository.getSumOfAllRevenuesByPeriod(start, end)
    }
}