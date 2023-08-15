package study.book.gyhdoca.application.service

import study.book.gyhdoca.application.port.`in`.GetAccountBalanceQuery
import study.book.gyhdoca.application.port.out.LoadAccountPort
import study.book.gyhdoca.domain.AccountId
import study.book.gyhdoca.domain.Money
import java.time.LocalDateTime

class GetAccountBalanceService(
    private val loadAccountPort: LoadAccountPort
) : GetAccountBalanceQuery {
    override fun getAccountBalance(accountId: AccountId): Money {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now())
            .calculateBalance()
    }
}