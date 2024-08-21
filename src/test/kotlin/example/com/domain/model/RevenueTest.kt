package example.com.domain.model

import example.com.presentation.route.requests.RevenueRequest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration

class RevenueTest {

    @Test
    fun `create should return a valid Revenue object`() {

        val request = RevenueRequest(100.0, 200.0, 300.0, 400.0)
        val username = "testUser"

        val revenue = Revenue.create(request, username)

        assertNotNull(revenue)
        assertEquals(0, revenue.id)
        assertTrue(
            revenue.date.toInstant(TimeZone.of("America/Sao_Paulo")).minus(Clock.System.now()) < Duration.parse("1s")
        )
        assertEquals(Money(BigDecimal("100.0")), revenue.money)
        assertEquals(Money(BigDecimal("200.0")), revenue.card)
        assertEquals(Money(BigDecimal("300.0")), revenue.pix)
        assertEquals(Money(BigDecimal("400.0")), revenue.voucher)
        assertEquals(username, revenue.registeredBy)
    }

    @Test
    fun `reconstitute should return a valid Revenue object`() {

        val id = 1
        val date = Clock.System.now().toLocalDateTime(TimeZone.of("America/Sao_Paulo"))
        val money = Money(BigDecimal("100.0"))
        val card = Money(BigDecimal("200.0"))
        val pix = Money(BigDecimal("300.0"))
        val voucher = Money(BigDecimal("400.0"))
        val registeredBy = "testUser"

        val revenue = Revenue.reconstitute(id, date, money, card, pix, voucher, registeredBy)

        assertNotNull(revenue)
        assertEquals(id, revenue.id)
        assertEquals(date, revenue.date)
        assertEquals(money, revenue.money)
        assertEquals(card, revenue.card)
        assertEquals(pix, revenue.pix)
        assertEquals(voucher, revenue.voucher)
        assertEquals(registeredBy, revenue.registeredBy)
    }

    @Test
    fun `total should return the sum of all money types`() {

        val revenue = Revenue.reconstitute(
            1,
            Clock.System.now().toLocalDateTime(TimeZone.of("America/Sao_Paulo")),
            Money(BigDecimal("100.0")),
            Money(BigDecimal("200.0")),
            Money(BigDecimal("300.0")),
            Money(BigDecimal("400.0")),
            "testUser"
        )

        val total = revenue.total

        assertEquals(Money(BigDecimal("1000.0")), total)
    }
}