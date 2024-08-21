package example.com.domain.model

import example.com.presentation.route.requests.RevenueRequest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class Revenue private constructor(
    override val id: Int,
    val date: LocalDateTime,
    val money: Money,
    val card: Money,
    val pix: Money,
    val voucher: Money,
    val registeredBy: String
) : Entity<Int>() {

    companion object {
        fun create(
            request: RevenueRequest,
            username: String
        ): Revenue {
            val currentMoment: Instant = Clock.System.now()
            val today: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.of("America/Sao_Paulo"))

            return Revenue(
                id = 0, // Assumindo que o ID será gerado pelo banco de dados
                date = today,
                money = Money(request.totalCashAmount.toBigDecimal()),
                card = Money(request.totalCardAmount.toBigDecimal()),
                pix = Money(request.totalPixAmount.toBigDecimal()),
                voucher = Money(request.totalVoucherAmount.toBigDecimal()),
                registeredBy = username
            )
        }

        fun reconstitute(
            id: Int,
            date: LocalDateTime,
            money: Money,
            card: Money,
            pix: Money,
            voucher: Money,
            registeredBy: String
        ): Revenue {
            return Revenue(
                id = id,
                date = date,
                money = money,
                card = card,
                pix = pix,
                voucher = voucher,
                registeredBy = registeredBy
            )
        }
    }

    val total: Money
        get() = money + card + pix + voucher


}
