package example.com.presentation.route.responses

import kotlinx.serialization.Serializable

@Serializable
data class PeriodTotalResponse(
    val startDate: String,
    val endDate: String,
    val revenue: Double,
    val expense: Double,
    val netBalance: Double
)
