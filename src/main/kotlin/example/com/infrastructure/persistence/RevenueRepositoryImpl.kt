package example.com.infrastructure.persistence

import example.com.domain.model.Money
import example.com.domain.model.Revenue
import example.com.domain.model.RevenueSummary
import example.com.domain.repository.RevenueRepository
import example.com.infrastructure.database.tables.RevenueTable
import example.com.presentation.route.responses.RevenueSummaryByRegistrarResponse
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class RevenueRepositoryImpl : RevenueRepository {

    override suspend fun create(revenue: Revenue) {
        transaction {
            RevenueTable.insert {
                it[date] = revenue.date
                it[totalCashAmount] = revenue.money.amount
                it[totalCardAmount] = revenue.card.amount
                it[totalPixAmount] = revenue.pix.amount
                it[totalVoucherAmount] = revenue.voucher.amount
                it[registeredBy] = revenue.registeredBy
            }
        }
    }

    override suspend fun getAllRevenues(): List<Revenue> {
        return transaction {
            RevenueTable.selectAll().map {
                Revenue.reconstitute(
                    id = it[RevenueTable.id],
                    date = it[RevenueTable.date],
                    money = Money(it[RevenueTable.totalCashAmount]),
                    card = Money(it[RevenueTable.totalCardAmount]),
                    pix = Money(it[RevenueTable.totalPixAmount]),
                    voucher = Money(it[RevenueTable.totalVoucherAmount]),
                    registeredBy = it[RevenueTable.registeredBy]
                )
            }
        }
    }

    override suspend fun getSumOfAllRevenuesByDate(date: LocalDate): Money =
        transaction {
            val sumOf = RevenueTable.selectAll()
                .where { RevenueTable.date.date() eq date }
                .sumOf {
                    it[RevenueTable.totalCashAmount] +
                            it[RevenueTable.totalCardAmount] +
                            it[RevenueTable.totalPixAmount] +
                            it[RevenueTable.totalVoucherAmount]
                }
            Money(sumOf)
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

    override suspend fun getSumOfAllRevenuesByPeriod(startDate: LocalDate, endDate: LocalDate): Money {
        return transaction {
            val sum = RevenueTable.selectAll()
                .where { (RevenueTable.date.date() greaterEq startDate) and (RevenueTable.date.date() lessEq endDate) }
                .sumOf {
                    it[RevenueTable.totalCashAmount] +
                            it[RevenueTable.totalCardAmount] +
                            it[RevenueTable.totalPixAmount] +
                            it[RevenueTable.totalVoucherAmount]
                }
            Money(sum)
        }
    }
}