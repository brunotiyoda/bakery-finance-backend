package example.com.infrastructure.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object RevenueTable : Table(name = "revenues") {
    val id = integer("id").autoIncrement()

    val date = datetime("data")
    val totalCashAmount = decimal("total_cash_amount", 10, 2)
    val totalCardAmount = decimal("total_card_amount", 10, 2)
    val totalPixAmount = decimal("total_pix_amount", 10, 2)
    val totalVoucherAmount = decimal("total_voucher_amount", 10, 2)
    val registeredBy = varchar("registered_by", 50)

    override val primaryKey = PrimaryKey(id)
}