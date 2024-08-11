package example.com.presentation.route.responses

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseResponse(
    val id: Int? = null,
    val data: LocalDateTime,
    val value: Double,
    val description: String,
    val registeredBy: String
) {

}
