package example.com.domain.model

import kotlinx.datetime.LocalDateTime
import org.junit.Assert.assertEquals
import java.math.BigDecimal
import kotlin.test.Test

class ExpenseTest {

    @Test
    fun `should create a valid expense`() {
        val expense = Expense(
            id = 1,
            date = LocalDateTime(2023, 8, 1, 10, 0),
            value = BigDecimal("100.00"),
            description = "Test expense",
            registeredBy = "jonhdoe"
        )

        assertEquals(1, expense.id)
        assertEquals(LocalDateTime(2023, 8, 1, 10, 0), expense.date)
        assertEquals(BigDecimal("100.00"), expense.value)
        assertEquals("Test expense", expense.description)
        assertEquals("jonhdoe", expense.registeredBy)
    }
}