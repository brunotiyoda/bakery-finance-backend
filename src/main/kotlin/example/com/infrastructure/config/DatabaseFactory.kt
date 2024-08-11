package example.com.infrastructure.config

import example.com.infrastructure.database.tables.ExpenseTable
import example.com.infrastructure.database.tables.RevenueTable
import example.com.infrastructure.database.tables.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val dbUrl = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/padarias_db"
        val dbUser = System.getenv("DB_USER") ?: "padaria"
        val dbPassword = System.getenv("DB_PASSWORD") ?: throw IllegalStateException("DB_PASSWORD não configurado")

        Database.connect(
            url = dbUrl,
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
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