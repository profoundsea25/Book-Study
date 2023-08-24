package study.book.gyhdoca.adapter.persistence

import org.springframework.stereotype.Component
import study.book.gyhdoca.domain.*

@Component
class AccountMapper {
    fun mapToDomainEntity(
        account: AccountJpaEntity,
        activities: List<ActivityJpaEntity>,
        withdrawalBalance: Long,
        depositBalance: Long,
    ): Account {
        val baselineBalance: Money = Money.subtract(
            Money.of(depositBalance),
            Money.of(withdrawalBalance))
        return Account.withId(
            AccountId(account.id),
            baselineBalance,
            mapToActivityWindow(activities)
        )
    }

    private fun mapToActivityWindow(activities: List<ActivityJpaEntity>): ActivityWindow {
        val mappedActivities: MutableList<Activity> = activities.map {
            Activity(
                ActivityId(it.id),
                AccountId(it.ownerAccountId),
                AccountId(it.sourceAccountId),
                AccountId(it.targetAccountId),
                it.timestamp,
                Money.of(it.amount)
            )
        }.toMutableList()
        return ActivityWindow(mappedActivities)
    }

    fun mapToJpaEntity(activity: Activity): ActivityJpaEntity {
        return ActivityJpaEntity(
            activity.id?.value ?: 0,
            activity.timestamp,
            activity.ownerAccountId.value,
            activity.sourceAccountId.value,
            activity.targetAccountId.value,
            activity.money.amount.toLong()
        )
    }
}
