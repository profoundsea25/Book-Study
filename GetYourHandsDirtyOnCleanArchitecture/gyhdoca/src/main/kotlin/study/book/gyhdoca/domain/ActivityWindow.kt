package study.book.gyhdoca.domain

import java.time.LocalDateTime
import java.util.*

data class ActivityWindow(
    private val activities: MutableList<Activity>
){
    constructor(vararg activities: Activity): this(activities.toMutableList())

    fun getStartTimestamp(): LocalDateTime {
        return activities.minOf { it.timestamp }
    }

    fun getEndTimestamp(): LocalDateTime {
        return activities.maxOf { it.timestamp }
    }

    fun calculateBalance(accountId: AccountId): Money {
        val depositBalance: Money = activities.asSequence()
            .filter { it.targetAccountId == accountId }
            .map { it.money }
            .reduce { a: Money, b: Money -> Money.add(a, b) }

        val withdrawalBalance: Money = activities.asSequence()
            .filter { it.sourceAccountId == accountId }
            .map { it.money }
            .reduce { a: Money, b: Money -> Money.add(a, b) }

        return Money.add(depositBalance, withdrawalBalance.negate())
    }

    fun getActivities(): List<Activity> {
        return Collections.unmodifiableList(this.activities)
    }

    fun addActivity(activity: Activity) {
        this.activities.add(activity)
    }

}
