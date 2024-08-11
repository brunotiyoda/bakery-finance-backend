package example.com.presentation.route.responses

import kotlinx.serialization.Serializable

@Serializable
data class MonthlyTotalResponse(
    val revenue: Double,
    val expense: Double,
    val netBalance: Double
)
