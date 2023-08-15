package study.book.gyhdoca.application.port.`in`

import study.book.gyhdoca.domain.AccountId
import study.book.gyhdoca.domain.Money
import study.book.gyhdoca.shared.SelfValidating

class SendMoneyCommand(
    private val sourceAccountId: AccountId,
    private val targetAccountId: AccountId,
    private val money: Money
) : SelfValidating<SendMoneyCommand>() {
    init {
        requireGreaterThan(money, 0)
        this.validateSelf()
    }
}