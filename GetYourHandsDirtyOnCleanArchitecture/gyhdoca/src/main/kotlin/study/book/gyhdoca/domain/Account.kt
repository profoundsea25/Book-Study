package study.book.gyhdoca.domain

import java.time.LocalDateTime

data class Account private constructor(
    val id: AccountId?,
    val activityWindow: ActivityWindow,
    private val baselineBalance: Money,
) {
    companion object {
        fun withoutId(
            baselineBalance: Money,
            activityWindow: ActivityWindow
        ): Account {
            return Account(
                id = null,
                baselineBalance = baselineBalance,
                activityWindow = activityWindow
            )
        }

        fun withId(
            accountId: AccountId,
            baselineBalance: Money,
            activityWindow: ActivityWindow
        ): Account {
            return Account(
                id = accountId,
                baselineBalance = baselineBalance,
                activityWindow = activityWindow
            )
        }
    }

    fun calculateBalance(): Money {
        return Money.add(
            this.baselineBalance,
            this.activityWindow.calculateBalance(this.id!!)
        )
    }

    fun withdraw(money: Money, targetAccountId: AccountId): Boolean {
        if (!mayWithdraw(money)) {
            return false
        }

        val withdrawal: Activity = Activity(
            ownerAccountId = this.id!!,
            sourceAccountId = this.id,
            targetAccountId = targetAccountId,
            timestamp = LocalDateTime.now(),
            money = money
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
            ownerAccountId = this.id!!,
            sourceAccountId = sourceAccountId,
            targetAccountId = this.id,
            timestamp = LocalDateTime.now(),
            money = money
        )
        this.activityWindow.addActivity(deposit)
        return true
    }
}
