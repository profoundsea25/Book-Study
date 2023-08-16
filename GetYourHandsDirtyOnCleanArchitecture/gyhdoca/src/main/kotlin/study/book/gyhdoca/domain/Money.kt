package study.book.gyhdoca.domain

import java.math.BigInteger

data class Money(
    private val amount: BigInteger
) {
    fun isPositiveOrZero(): Boolean {
        return this.amount >= BigInteger.ZERO
    }

    fun isNegative(): Boolean {
        return this.amount < BigInteger.ZERO
    }

    fun isPositive(): Boolean {
        return this.amount > BigInteger.ZERO
    }

    fun isGreaterThanOrEqualTo(money: Money): Boolean {
        return this.amount >= money.amount
    }

    fun isGreaterThan(money: Money): Boolean {
        return this.amount > money.amount
    }

    fun minus(money: Money): Money {
        return Money(this.amount.subtract(money.amount))
    }

    fun plus(money: Money): Money {
        return Money(this.amount.add(money.amount))
    }

    fun negate(): Money {
        return Money(this.amount.negate())
    }

    companion object {
        val ZERO: Money = of(0L)

        fun of(value: Long): Money {
            return Money(BigInteger.valueOf(value))
        }

        fun add(a: Money, b: Money): Money {
            return Money(a.amount.add(b.amount))
        }

        fun substract(a: Money, b: Money): Money {
            return Money(a.amount.subtract(b.amount))
        }
    }
}
