package study.book.gyhdoca.domain

import java.time.LocalDateTime
import java.util.*

data class ActivityWindow(
    private val _activities: MutableList<Activity>
){
    constructor(vararg activities: Activity): this(activities.toMutableList())

    fun getStartTimestamp(): LocalDateTime {
        return _activities.minOf { it.timestamp }
    }

    fun getEndTimestamp(): LocalDateTime {
        return _activities.maxOf { it.timestamp }
    }

    fun calculateBalance(accountId: AccountId): Money {
        val depositBalance: Money = _activities.asSequence()
            .filter { it.targetAccountId == accountId }
            .map { it.money }
            .reduceOrNull(Money::add) ?: Money.ZERO

        val withdrawalBalance: Money = _activities.asSequence()
            .filter { it.sourceAccountId == accountId }
            .map { it.money }
            .reduceOrNull(Money::add) ?: Money.ZERO

        return Money.add(depositBalance, withdrawalBalance.negate())
    }

    val activities: List<Activity> = Collections.unmodifiableList(this._activities)

    fun addActivity(activity: Activity) {
        this._activities.add(activity)
    }
}
