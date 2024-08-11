package example.com.domain.model

import kotlinx.datetime.LocalDate
import java.math.BigDecimal

data class DailyReportItem(
    val date: LocalDate,
    val revenue: BigDecimal,
    val expense: BigDecimal,
    val netBalance: BigDecimal
)
