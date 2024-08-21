package example.com.infrastructure.database.tables

import org.jetbrains.exposed.sql.Table

object UserTable : Table(name = "users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50)
    val password = varchar("password", 64)
    val role = varchar("role", 20)

    override val primaryKey = PrimaryKey(id)
}