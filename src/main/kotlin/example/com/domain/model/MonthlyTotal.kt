package example.com.domain.model

import java.math.BigDecimal

data class MonthlyTotal(
    val revenues: BigDecimal,
    val expenses: BigDecimal,
    val netBalance: BigDecimal
)
