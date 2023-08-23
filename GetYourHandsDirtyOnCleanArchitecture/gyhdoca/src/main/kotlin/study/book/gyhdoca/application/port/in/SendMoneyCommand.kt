package study.book.gyhdoca.application.port.`in`

import study.book.gyhdoca.domain.AccountId
import study.book.gyhdoca.domain.Money
import study.book.gyhdoca.shared.SelfValidating

data class SendMoneyCommand(
    val sourceAccountId: AccountId,
    val targetAccountId: AccountId,
    val money: Money
) : SelfValidating<SendMoneyCommand>() {
    init {
        requireGreaterThan(money, 0)
        this.validateSelf()
    }

    private fun requireGreaterThan(money: Money, i: Int) {
    }
}
