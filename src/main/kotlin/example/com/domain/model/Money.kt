package example.com.domain.model

import java.math.BigDecimal

data class Money(val amount: BigDecimal) {
    init {
        require(amount >= BigDecimal.ZERO) { "Amount must be non-negative" }
    }

    operator fun plus(other: Money) = Money(this.amount + other.amount)
    operator fun minus(other: Money) = Money(this.amount - other.amount)

    companion object {
        val ZERO = Money(BigDecimal.ZERO)
    }
}

// encriptar password na tela de login