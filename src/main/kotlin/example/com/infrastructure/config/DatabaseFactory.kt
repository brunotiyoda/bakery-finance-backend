package example.com.infrastructure.config

import example.com.infrastructure.database.tables.ExpenseTable
import example.com.infrastructure.database.tables.RevenueTable
import example.com.infrastructure.database.tables.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect(
            url = "jdbc:pgsql://localhost:5432/padarias_db",
            driver = "org.postgresql.Driver",
            user = "padaria",
            password = "k8jVn%L^2\$)vcY<6"
        )
        transaction {
            SchemaUtils.create(
                UserTable,
                RevenueTable,
                ExpenseTable
            )
        }
    }
}