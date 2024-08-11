package example.com.presentation.route.responses

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class RevenueResponse(
    val id: Int?,
    val date: LocalDateTime,
    val totalCashAmount: Double,
    val totalCardAmount: Double,
    val totalPixAmount: Double,
    val totalVoucherAmount: Double,
    val registeredBy: String
) {
}

