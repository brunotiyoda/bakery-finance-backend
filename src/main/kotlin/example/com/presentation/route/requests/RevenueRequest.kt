package example.com.presentation.route.requests

import example.com.domain.model.RevenueCategory
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class RevenueRequest(
    val value: Double,
    val category: RevenueCategory,
    val revenueDate: LocalDate? = null
)

@Serializable
data class RevenueRequestList(
    val revenues: List<RevenueRequest>
)