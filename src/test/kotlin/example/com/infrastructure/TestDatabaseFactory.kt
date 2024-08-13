package example.com.infrastructure

import example.com.infrastructure.database.tables.ExpenseTable
import example.com.infrastructure.database.tables.RevenueTable
import example.com.infrastructure.database.tables.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object TestDatabaseFactory {
    fun init() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        transaction {
            SchemaUtils.create(UserTable, RevenueTable, ExpenseTable)
        }
    }
}