package study.book.gyhdoca.domain

import java.time.LocalDateTime

data class Activity(
    val ownerAccountId: AccountId,
    val sourceAccountId: AccountId,
    val targetAccountId: AccountId,
    val timestamp: LocalDateTime,
    val money: Money
) {
    var id: ActivityId? = null
}
