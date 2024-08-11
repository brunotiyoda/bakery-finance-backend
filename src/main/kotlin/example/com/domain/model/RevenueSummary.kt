package example.com.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RevenueSummary(
    val pix: Double,
    val card: Double,
    val money: Double,
    val voucher: Double,
    val total: Double
)

