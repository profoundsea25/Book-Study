package study.book.gyhdoca.domain

import java.time.LocalDateTime

class Account(
    private val id: AccountId,
    private val baselineBalance: Money,
    private val activityWindow: ActivityWindow,
) {

    fun calculateBalance(): Money {
        return Money.add(
            this.baselineBalance,
            this.activityWindow.calculateBalance(this.id)
        )
    }

    fun withdraw(money: Money, targetAccountId: AccountId): Boolean {
        if (!mayWithdraw(money)) {
            return false
        }

        val withdrawal: Activity = Activity(
            this.id,
            this.id,
            targetAccountId,
            LocalDateTime.now(),
            money
        )
        this.activityWindow.addActivity(withdrawal)
        return true
    }

    private fun mayWithdraw(money: Money): Boolean {
        return Money.add(
            this.calculateBalance(),
            money.negate()
        ).isPositive()
    }

    fun deposit(money: Money, sourceAccountId: AccountId): Boolean {
        val deposit: Activity = Activity(
            this.id,
            sourceAccountId,
            this.id,
            LocalDateTime.now(),
            money
        )
        this.activityWindow.addActivity(deposit)
        return true
    }
}
