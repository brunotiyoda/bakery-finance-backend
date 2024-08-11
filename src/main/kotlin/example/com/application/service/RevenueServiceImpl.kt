package example.com.application.service

import example.com.domain.model.Revenue
import example.com.domain.repository.RevenueRepository
import example.com.domain.service.RevenueService
import example.com.presentation.route.requests.RevenueRequest
import example.com.presentation.route.responses.RevenueSummaryByRegistrarResponse
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.math.BigDecimal

class RevenueServiceImpl(
    private val repository: RevenueRepository
) : RevenueService {

    override suspend fun createRevenue(request: RevenueRequest, username: String) {
        val currentMoment: Instant = Clock.System.now()
        val today: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.of("America/Sao_Paulo"))

        val revenue = Revenue(
            date = today,
            money = request.totalCashAmount.toBigDecimal(),
            card = request.totalCardAmount.toBigDecimal(),
            pix = request.totalPixAmount.toBigDecimal(),
            voucher = request.totalVoucherAmount.toBigDecimal(),
            registeredBy = username
        )

        repository.create(revenue)
    }

    override suspend fun getAllRevenues(): List<Revenue> {
        return repository.getAllRevenues()
    }

    override suspend fun getSumOfAllRevenuesByDate(date: String): BigDecimal {
        val localDate = LocalDate.parse(date)
        return repository.getSumOfAllRevenuesByDate(localDate)
    }

    override suspend fun getSumOfRevenuesByRegistrarAndDate(date: String): RevenueSummaryByRegistrarResponse {
        val localDate = LocalDate.parse(date)
        return repository.getSumOfRevenuesByRegistrarAndDate(localDate)
    }

    override suspend fun getSumOfAllRevenuesByPeriod(startDate: String, endDate: String): BigDecimal {
        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        return repository.getSumOfAllRevenuesByPeriod(start, end)
    }

    override suspend fun getSumOfAllRevenuesByDateRange(startDate: LocalDate, endDate: LocalDate): BigDecimal {
        TODO("Not yet implemented")
    }
}