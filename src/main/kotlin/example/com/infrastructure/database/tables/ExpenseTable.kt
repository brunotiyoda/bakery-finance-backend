package example.com.infrastructure.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ExpenseTable : Table(name = "expenses") {
    val id = integer("id").autoIncrement()

    val date = datetime("data")
    val value = decimal("value", 10, 2)
    val description = varchar("description", length = 250)
    val registeredBy = varchar("registered_by", length = 50)

    override val primaryKey = PrimaryKey(RevenueTable.id)
}