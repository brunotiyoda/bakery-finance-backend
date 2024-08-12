package example.com.domain.repository

import example.com.domain.model.Expense
import kotlinx.datetime.LocalDate
import java.math.BigDecimal

interface ExpenseRepository {
    suspend fun create(expenses: List<Expense>)
    suspend fun getAllExpensesByDate(dateInformed: LocalDate): List<Expense>
    suspend fun getSumOfExpensesByDate(date: LocalDate): BigDecimal
    suspend fun getSumOfExpensesByPeriod(startDate: LocalDate, endDate: LocalDate): BigDecimal
}