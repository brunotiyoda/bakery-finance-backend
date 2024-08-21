package example.com.infrastructure.persistence

import example.com.domain.model.Money
import example.com.domain.model.Revenue
import example.com.infrastructure.database.tables.RevenueTable
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RevenueRepositoryImplTest {

    private lateinit var repository: RevenueRepositoryImpl

    @Before
    fun setup() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(RevenueTable)
        }
        repository = RevenueRepositoryImpl()
    }

    @Test
    fun `create should insert revenue into database`() = runBlocking {

        val revenue = Revenue.reconstitute(
            id = 0,
            date = LocalDateTime.parse("2023-08-21T10:15:30"),
            money = Money(BigDecimal("100.00")),
            card = Money(BigDecimal("200.00")),
            pix = Money(BigDecimal("300.00")),
            voucher = Money(BigDecimal("400.00")),
            registeredBy = "testUser"
        )

        repository.create(revenue)

        val insertedRevenue = transaction {
            RevenueTable.selectAll().single()
        }

        assertEquals(revenue.date, insertedRevenue[RevenueTable.date])
        assertEquals(revenue.money.amount, insertedRevenue[RevenueTable.totalCashAmount])
        assertEquals(revenue.card.amount, insertedRevenue[RevenueTable.totalCardAmount])
        assertEquals(revenue.pix.amount, insertedRevenue[RevenueTable.totalPixAmount])
        assertEquals(revenue.voucher.amount, insertedRevenue[RevenueTable.totalVoucherAmount])
        assertEquals(revenue.registeredBy, insertedRevenue[RevenueTable.registeredBy])

        cleanDatabase()
    }

    @Test
    fun `getAllRevenues should return all revenues from database`() = runBlocking {

        val revenue1 = createTestRevenue(1)
        val revenue2 = createTestRevenue(2)
        transaction {
            RevenueTable.insert {
                it[date] = revenue1.date
                it[totalCashAmount] = revenue1.money.amount
                it[totalCardAmount] = revenue1.card.amount
                it[totalPixAmount] = revenue1.pix.amount
                it[totalVoucherAmount] = revenue1.voucher.amount
                it[registeredBy] = revenue1.registeredBy
            }
            RevenueTable.insert {
                it[date] = revenue2.date
                it[totalCashAmount] = revenue2.money.amount
                it[totalCardAmount] = revenue2.card.amount
                it[totalPixAmount] = revenue2.pix.amount
                it[totalVoucherAmount] = revenue2.voucher.amount
                it[registeredBy] = revenue2.registeredBy
            }
        }

        val result = repository.getAllRevenues()

        assertEquals(2, result.size)

        cleanDatabase()
    }

    @Test
    fun `getSumOfAllRevenuesByDate should return correct sum`() = runBlocking {

        val date = LocalDate.parse("2023-08-21")
        val revenue1 = createTestRevenue(1, date.atTime(10, 0))
        val revenue2 = createTestRevenue(2, date.atTime(14, 0))
        insertTestRevenues(revenue1, revenue2)

        val result = repository.getSumOfAllRevenuesByDate(date)

        assertEquals(Money(BigDecimal("2000.00")), result)

        cleanDatabase()
    }

    @Test
    fun `getSumOfRevenuesByRegistrarAndDate should return correct summary`() = runBlocking {

        val date = LocalDate.parse("2023-08-21")
        val revenue1 = createTestRevenue(1, date.atTime(10, 0), "user1")
        val revenue2 = createTestRevenue(2, date.atTime(14, 0), "user2")
        insertTestRevenues(revenue1, revenue2)

        val result = repository.getSumOfRevenuesByRegistrarAndDate(date)

        assertEquals(date, result.date)
        assertEquals(2, result.summaries.size)
        assertTrue(result.summaries.containsKey("user1"))
        assertTrue(result.summaries.containsKey("user2"))

        cleanDatabase()
    }

    @Test
    fun `getSumOfAllRevenuesByPeriod should return correct sum`() = runBlocking {

        val startDate = LocalDate.parse("2023-08-21")
        val endDate = LocalDate.parse("2023-08-23")
        val revenue1 = createTestRevenue(1, startDate.atTime(10, 0))
        val revenue2 = createTestRevenue(2, endDate.atTime(14, 0))
        insertTestRevenues(revenue1, revenue2)

        val result = repository.getSumOfAllRevenuesByPeriod(startDate, endDate)

        assertEquals(Money(BigDecimal("2000.00")), result)

        cleanDatabase()
    }

    private fun createTestRevenue(
        id: Int,
        date: LocalDateTime = LocalDateTime.parse("2023-08-21T10:15:30"),
        user: String = "testUser"
    ): Revenue {
        return Revenue.reconstitute(
            id = id,
            date = date,
            money = Money(BigDecimal("250.00")),
            card = Money(BigDecimal("250.00")),
            pix = Money(BigDecimal("250.00")),
            voucher = Money(BigDecimal("250.00")),
            registeredBy = user
        )
    }

    private fun insertTestRevenues(vararg revenues: Revenue) {
        transaction {
            revenues.forEach { revenue ->
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
    }

    private fun cleanDatabase() {
        transaction {
            RevenueTable.deleteAll()
        }
    }
}