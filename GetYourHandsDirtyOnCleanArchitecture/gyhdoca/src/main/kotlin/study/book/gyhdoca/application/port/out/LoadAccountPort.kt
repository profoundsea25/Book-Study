package study.book.gyhdoca.application.port.out

import study.book.gyhdoca.domain.AccountId
import study.book.gyhdoca.domain.Money
import java.time.LocalDateTime

interface LoadAccountPort {
    fun loadAccount(accountId: AccountId, localDateTime: LocalDateTime): Money
}
