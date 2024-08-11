package example.com.presentation.route.responses

import example.com.domain.model.RevenueSummary
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class RevenueSummaryByRegistrarResponse(
    val date: LocalDate,
    val summaries: Map<String, RevenueSummary>
)
