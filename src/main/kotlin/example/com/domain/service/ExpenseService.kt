package example.com.domain.service

import example.com.domain.model.Expense
import example.com.presentation.route.requests.ExpenseRequestWrapper
import kotlinx.datetime.LocalDate
import java.math.BigDecimal

interface ExpenseService {
    suspend fun createExpenses(expensesRequest: ExpenseRequestWrapper, username: String)
    suspend fun getAllExpenses(): List<Expense>
    suspend fun getSumOfAllExpensesByDate(date: String): BigDecimal
    suspend fun getSumOfAllExpensesByPeriod(startDate: String, endDate: String): BigDecimal
    suspend fun getSumOfAllExpensesByDateRange(startDate: LocalDate, endDate: LocalDate): BigDecimal
}