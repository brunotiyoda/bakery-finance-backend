package example.com.domain.model

import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

data class Revenue(
    val id: Int? = null,
    val date: LocalDateTime,
    val money: BigDecimal,
    val card: BigDecimal,
    val pix: BigDecimal,
    val voucher: BigDecimal,
    val registeredBy: String
)
