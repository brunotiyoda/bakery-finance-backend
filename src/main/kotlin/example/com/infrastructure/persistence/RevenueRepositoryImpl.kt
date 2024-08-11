package example.com.infrastructure.persistence

import example.com.domain.model.Revenue
import example.com.domain.repository.RevenueRepository
import example.com.infrastructure.database.tables.RevenueTable
import example.com.domain.model.RevenueSummary
import example.com.presentation.route.responses.RevenueSummaryByRegistrarResponse
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class RevenueRepositoryImpl : RevenueRepository {

    override suspend fun create(revenue: Revenue) {
        transaction {
            RevenueTable.insert {
                it[date] = revenue.date
                it[totalCashAmount] = revenue.money
                it[totalCardAmount] = revenue.card
                it[totalPixAmount] = revenue.pix
                it[totalVoucherAmount] = revenue.voucher
                it[registeredBy] = revenue.registeredBy
            }
        }
    }

    override suspend fun getAllRevenues(): List<Revenue> {
        return transaction {
            RevenueTable.selectAll().map {
                Revenue(
                    id = it[RevenueTable.id],
                    date = it[RevenueTable.date],
                    money = it[RevenueTable.totalCashAmount],
                    card = it[RevenueTable.totalCardAmount],
                    pix = it[RevenueTable.totalPixAmount],
                    voucher = it[RevenueTable.totalVoucherAmount],
                    registeredBy = it[RevenueTable.registeredBy]
                )
            }
        }
    }

    override suspend fun getSumOfAllRevenuesByDate(date: LocalDate) =
        transaction {
            val sumOf = RevenueTable.selectAll()
                .where { RevenueTable.date.date() eq date }
                .map {
                    Revenue(
                        id = it[RevenueTable.id],
                        date = it[RevenueTable.date],
                        money = it[RevenueTable.totalCashAmount],
                        card = it[RevenueTable.totalCardAmount],
                        pix = it[RevenueTable.totalPixAmount],
                        voucher = it[RevenueTable.totalVoucherAmount],
                        registeredBy = it[RevenueTable.registeredBy]
                    )
                }.sumOf { revenue ->
                    revenue.pix + revenue.card + revenue.money + revenue.voucher
                }
            return@transaction sumOf
        }

    override suspend fun getSumOfRevenuesByRegistrarAndDate(date: LocalDate): RevenueSummaryByRegistrarResponse {
        return transaction {
            val summaries = RevenueTable.selectAll()
                .where { RevenueTable.date.date() eq date }
                .groupBy { it[RevenueTable.registeredBy] }
                .mapValues { (_, rows) ->
                    RevenueSummary(
                        pix = rows.sumOf { it[RevenueTable.totalPixAmount].toDouble() },
                        card = rows.sumOf { it[RevenueTable.totalCardAmount].toDouble() },
                        money = rows.sumOf { it[RevenueTable.totalCashAmount].toDouble() },
                        voucher = rows.sumOf { it[RevenueTable.totalVoucherAmount].toDouble() },
                        total = rows.sumOf {
                            it[RevenueTable.totalPixAmount].toDouble() +
                                    it[RevenueTable.totalCardAmount].toDouble() +
                                    it[RevenueTable.totalCashAmount].toDouble() +
                                    it[RevenueTable.totalVoucherAmount].toDouble()
                        }
                    )
                }
            RevenueSummaryByRegistrarResponse(date, summaries)
        }
    }

    override suspend fun getSumOfAllRevenuesByPeriod(startDate: LocalDate, endDate: LocalDate): BigDecimal {
        return transaction {
            RevenueTable.selectAll()
                .where { (RevenueTable.date.date() greaterEq startDate) and (RevenueTable.date.date() lessEq endDate) }
                .map {
                    Revenue(
                        id = it[RevenueTable.id],
                        date = it[RevenueTable.date],
                        money = it[RevenueTable.totalCashAmount],
                        card = it[RevenueTable.totalCardAmount],
                        pix = it[RevenueTable.totalPixAmount],
                        voucher = it[RevenueTable.totalVoucherAmount],
                        registeredBy = it[RevenueTable.registeredBy]
                    )
                }
                .sumOf { revenue ->
                    revenue.money + revenue.card + revenue.pix + revenue.voucher
                }
        }
    }
}