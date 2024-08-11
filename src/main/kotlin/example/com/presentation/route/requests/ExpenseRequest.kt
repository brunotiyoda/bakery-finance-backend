package example.com.presentation.route.requests

import kotlinx.serialization.Serializable

@Serializable
data class ExpenseRequest(
    val value: Double,
    val description: String
) {

}
