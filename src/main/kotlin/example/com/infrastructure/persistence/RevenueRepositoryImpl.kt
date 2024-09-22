package example.com.infrastructure.persistence

import example.com.domain.model.Money
import example.com.domain.model.Revenue
import example.com.domain.model.RevenueCategory
import example.com.domain.repository.RevenueRepository
import example.com.infrastructure.database.tables.RevenueTable
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
                it[registrationDate] = revenue.registrationDate
                it[revenueDate] = revenue.revenueDate
                it[value] = revenue.value.amount
                it[category] = revenue.category.name
                it[registeredBy] = revenue.registeredBy
            }
        }
    }

    override suspend fun getAllRevenues(): List<Revenue> {
        return transaction {
            RevenueTable.selectAll().map {
                Revenue.reconstitute(
                    id = it[RevenueTable.id],
                    registrationDate = it[RevenueTable.registrationDate],
                    revenueDate = it[RevenueTable.revenueDate],
                    value = Money(it[RevenueTable.value]),
                    category = RevenueCategory.valueOf(it[RevenueTable.category]),
                    registeredBy = it[RevenueTable.registeredBy]
                )
            }
        }
    }

    override suspend fun getSumOfAllRevenuesByDate(date: LocalDate): Money =
        transaction {
            val sumOf = RevenueTable.selectAll()
                .where { RevenueTable.registrationDate.date() eq date }
                .sumOf { it[RevenueTable.value] }
            Money(sumOf)
        }

    override suspend fun getSumOfRevenuesByRegistrarAndDate(date: LocalDate): Map<String, Map<RevenueCategory, Money>> {
        return transaction {
            RevenueTable
                .selectAll()
                .where { RevenueTable.revenueDate eq date }
                .groupBy(
                    { Triple(it[RevenueTable.registeredBy], it[RevenueTable.category], it[RevenueTable.value]) },
                    { it[RevenueTable.value] }
                )
                .entries
                .groupBy(
                    { it.key.first },  // registeredBy
                    {
                        val category = RevenueCategory.valueOf(it.key.second)
                        val sum = it.value.sumOf { value ->
                            when (value) {
                                is BigDecimal -> value
                                is Double -> BigDecimal.valueOf(value)
                                is Float -> BigDecimal.valueOf(value.toDouble())
                                is Int -> BigDecimal(value)
                                is Long -> BigDecimal(value)
                                else -> throw IllegalArgumentException("Unsupported type for value: ${value::class.java}")
                            }
                        }
                        category to Money(sum)
                    }
                )
                .mapValues { (_, list) -> list.toMap() }
        }
    }

    override suspend fun getSumOfAllRevenuesByPeriod(startDate: LocalDate, endDate: LocalDate): Money {
        return transaction {
            val sum = RevenueTable.selectAll()
                .where {
                    (RevenueTable.registrationDate.date() greaterEq startDate) and (RevenueTable.registrationDate.date() lessEq endDate)
                }
                .sumOf { it[RevenueTable.value] }
            Money(sum)
        }
    }
}