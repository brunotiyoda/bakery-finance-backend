package example.com.application.service

import example.com.domain.model.Expense
import example.com.domain.repository.ExpenseRepository
import example.com.domain.service.ExpenseService
import example.com.presentation.route.requests.ExpenseRequestWrapper
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.math.BigDecimal

class ExpenseServiceImpl(
    private val repository: ExpenseRepository
) : ExpenseService {

    override suspend fun createExpenses(expensesRequest: ExpenseRequestWrapper, username: String) {
        val currentMoment: Instant = Clock.System.now()
        val today: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.of("America/Sao_Paulo"))

        val listOfExpense = mutableListOf<Expense>()

        val expenses = expensesRequest.expenses.map { expense ->
            Expense(
                date = today,
                value = expense.value.toBigDecimal(),
                description = expense.description.trim(),
                registeredBy = username
            )
        }

        listOfExpense.addAll(expenses)

        repository.create(listOfExpense)
    }

    override suspend fun getAllExpensesByDate(date: String): List<Expense> {
        val dateInformed = LocalDate.parse(date)
        return repository.getAllExpensesByDate(dateInformed)
    }

    override suspend fun getSumOfAllExpensesByDate(date: String): BigDecimal {
        val localDate = LocalDate.parse(date)
        return repository.getSumOfExpensesByDate(localDate)
    }

    override suspend fun getSumOfAllExpensesByPeriod(startDate: String, endDate: String): BigDecimal {
        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        return repository.getSumOfExpensesByPeriod(start, end)
    }

    override suspend fun getSumOfAllExpensesByDateRange(startDate: LocalDate, endDate: LocalDate): BigDecimal {
        TODO("Not yet implemented")
    }
}