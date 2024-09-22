package example.com.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class Revenue private constructor(
    override val id: Int,
    val registrationDate: LocalDateTime,
    val revenueDate: LocalDate,
    val value: Money,
    val category: RevenueCategory,
    val registeredBy: String
) : Entity<Int>() {

    companion object {
        fun create(
            revenueDate: LocalDate?,
            value: Money,
            category: RevenueCategory,
            registeredBy: String
        ): Revenue {
            val now = Clock.System.now()
            val registrationDateTime = now.toLocalDateTime(TimeZone.of("America/Sao_Paulo"))
            val effectiveRevenueDate = revenueDate ?: registrationDateTime.date

            return Revenue(
                id = 0, // Assumindo que o ID será gerado pelo banco de dados
                registrationDate = registrationDateTime,
                revenueDate = effectiveRevenueDate,
                value = value,
                category = category,
                registeredBy = registeredBy
            )
        }

        fun reconstitute(
            id: Int,
            registrationDate: LocalDateTime,
            revenueDate: LocalDate,
            value: Money,
            category: RevenueCategory,
            registeredBy: String
        ): Revenue {
            return Revenue(
                id = id,
                registrationDate = registrationDate,
                revenueDate = revenueDate,
                value = value,
                category = category,
                registeredBy = registeredBy
            )
        }
    }
}
