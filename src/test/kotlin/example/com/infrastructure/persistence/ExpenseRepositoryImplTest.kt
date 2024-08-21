package example.com.infrastructure.persistence

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import java.math.BigDecimal
import kotlin.test.Test


class ExpenseRepositoryImplTest {

    private val repository: ExpenseRepositoryImpl = mockk()

    @Test
    fun `should get sum of expenses by date`() = runBlocking {
        val date = LocalDate(2023, 8, 1)
        coEvery { repository.getSumOfExpensesByDate(date) } returns BigDecimal("300.00")

        val result = repository.getSumOfExpensesByDate(date)

        assertEquals(BigDecimal("300.00"), result)
    }
}
