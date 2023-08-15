package study.book.gyhdoca.application.port.`in`

import study.book.gyhdoca.domain.AccountId
import study.book.gyhdoca.domain.Money

interface GetAccountBalanceQuery {
    fun getAccountBalance(accountId: AccountId): Money
}