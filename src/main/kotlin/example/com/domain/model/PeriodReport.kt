package example.com.domain.model

import kotlinx.datetime.LocalDate
import java.math.BigDecimal

data class PeriodReport(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dailyReports: List<DailyReportItem>,
    val totalRevenue: BigDecimal,
    val totalExpense: BigDecimal,
    val totalNetBalance: BigDecimal
)