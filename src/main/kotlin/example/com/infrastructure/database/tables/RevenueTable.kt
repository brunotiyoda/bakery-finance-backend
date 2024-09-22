package example.com.infrastructure.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object RevenueTable : Table(name = "revenues") {
    val id = integer("id").autoIncrement()

    val registrationDate = datetime("registration_date")
    val revenueDate = date("revenue_date")
    val value = decimal("value", precision = 10, scale = 2)
    val category = varchar("category", length = 50)
    val registeredBy = varchar("registered_by", length = 50)

    override val primaryKey = PrimaryKey(id)
}