package example.com.infrastructure.persistence

import example.com.domain.model.User
import example.com.domain.repository.UserRepository
import example.com.infrastructure.database.tables.UserTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepositoryImpl : UserRepository {
    override suspend fun create(user: User): User? = transaction {
        val insertStatement = UserTable.insert {
            it[username] = user.username
            it[password] = user.password
            it[role] = user.role
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    override suspend fun findByUsername(username: String): User? = transaction {
        UserTable.selectAll().where { UserTable.username eq username }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[UserTable.id],
        username = row[UserTable.username],
        password = row[UserTable.password],
        role = row[UserTable.role]
    )
}