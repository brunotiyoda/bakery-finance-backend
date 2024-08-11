package example.com.presentation.route.requests

import kotlinx.serialization.Serializable

@Serializable
data class RevenueRequest(
    val totalCashAmount: Double,
    val totalCardAmount: Double,
    val totalPixAmount: Double,
    val totalVoucherAmount: Double
) {
}