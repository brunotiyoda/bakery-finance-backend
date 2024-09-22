package example.com.presentation.route.responses

import example.com.domain.model.Revenue
import example.com.domain.model.RevenueCategory
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class RevenueResponse(
    val id: Int,
    val registrationDate: LocalDateTime,
    val revenueDate: LocalDate?,
    val value: Double,
    val category: RevenueCategory,
    val registeredBy: String
) {
    companion object {
        fun fromDomain(revenue: Revenue): RevenueResponse {
            return RevenueResponse(
                id = revenue.id,
                registrationDate = revenue.registrationDate,
                revenueDate = revenue.revenueDate,
                value = revenue.value.amount.toDouble(),
                category = revenue.category,
                registeredBy = revenue.registeredBy
            )
        }
    }
}

