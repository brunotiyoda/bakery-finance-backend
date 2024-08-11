package example.com.presentation.route.requests

import kotlinx.serialization.Serializable

@Serializable
data class ExpenseRequestWrapper(
    val expenses: List<ExpenseRequest>
)
