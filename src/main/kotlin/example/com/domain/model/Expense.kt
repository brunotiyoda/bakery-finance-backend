package example.com.domain.model

import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

data class Expense(
    val id: Int? = null,
    val date: LocalDateTime,
    val value: BigDecimal,
    val description: String,
    val registeredBy: String
)
