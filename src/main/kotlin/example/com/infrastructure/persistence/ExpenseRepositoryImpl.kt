package example.com.infrastructure.persistence

import example.com.domain.model.Expense
import example.com.domain.repository.ExpenseRepository
import example.com.infrastructure.database.tables.ExpenseTable
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class ExpenseRepositoryImpl : ExpenseRepository {

    override suspend fun create(expenses: List<Expense>) {
        transaction {
            expenses.forEach { expense ->
                ExpenseTable.insert {
                    it[date] = expense.date
                    it[value] = expense.value
                    it[description] = expense.description
                    it[registeredBy] = expense.registeredBy
                }
            }
        }
    }

    override suspend fun getAllExpensesByDate(dateInformed: LocalDate): List<Expense> {
        return transaction {
            ExpenseTable.selectAll()
                .where { ExpenseTable.date.date() eq dateInformed }
                .map {
                    Expense(
                        id = it[ExpenseTable.id],
                        date = it[ExpenseTable.date],
                        value = it[ExpenseTable.value],
                        description = it[ExpenseTable.description],
                        registeredBy = it[ExpenseTable.registeredBy]
                    )
                }
        }
    }

    override suspend fun getSumOfExpensesByDate(date: LocalDate) =
        transaction {
            val sumOf = ExpenseTable.selectAll()
                .where { ExpenseTable.date.date() eq date }
                .map {
                    Expense(
                        id = it[ExpenseTable.id],
                        date = it[ExpenseTable.date],
                        value = it[ExpenseTable.value],
                        description = it[ExpenseTable.description],
                        registeredBy = it[ExpenseTable.registeredBy]
                    )
                }
                .sumOf { expense ->
                    expense.value
                }
            return@transaction sumOf
        }

    override suspend fun getSumOfExpensesByPeriod(startDate: LocalDate, endDate: LocalDate): BigDecimal {
        return transaction {
            ExpenseTable.selectAll()
                .where { (ExpenseTable.date.date() greaterEq startDate) and (ExpenseTable.date.date() lessEq endDate) }
                .map {
                    Expense(
                        id = it[ExpenseTable.id],
                        date = it[ExpenseTable.date],
                        value = it[ExpenseTable.value],
                        description = it[ExpenseTable.description],
                        registeredBy = it[ExpenseTable.registeredBy]
                    )
                }
                .sumOf { expense ->
                    expense.value
                }
        }
    }
}