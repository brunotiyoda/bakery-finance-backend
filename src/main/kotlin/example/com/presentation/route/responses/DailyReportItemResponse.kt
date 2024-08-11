package example.com.presentation.route.responses

import example.com.domain.model.DailyReportItem
import kotlinx.serialization.Serializable

@Serializable
data class DailyReportItemResponse(
    val date: String,
    val revenue: Double,
    val expense: Double,
    val netBalance: Double
) {
    companion object {
        fun fromDomainModel(item: DailyReportItem): DailyReportItemResponse {
            return DailyReportItemResponse(
                date = item.date.toString(),
                revenue = item.revenue.toDouble(),
                expense = item.expense.toDouble(),
                netBalance = item.netBalance.toDouble()
            )
        }
    }
}
