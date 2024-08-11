package example.com.presentation.route.responses

import example.com.domain.model.PeriodReport
import kotlinx.serialization.Serializable

@Serializable
data class PeriodReportResponse(
    val startDate: String,
    val endDate: String,
    val dailyReports: List<DailyReportItemResponse>,
    val totalRevenue: Double,
    val totalExpense: Double,
    val totalNetBalance: Double
) {
    companion object {
        fun fromDomainModel(periodReport: PeriodReport): PeriodReportResponse {
            return PeriodReportResponse(
                startDate = periodReport.startDate.toString(),
                endDate = periodReport.endDate.toString(),
                dailyReports = periodReport.dailyReports.map { DailyReportItemResponse.fromDomainModel(it) },
                totalRevenue = periodReport.totalRevenue.toDouble(),
                totalExpense = periodReport.totalExpense.toDouble(),
                totalNetBalance = periodReport.totalNetBalance.toDouble()
            )
        }
    }
}
