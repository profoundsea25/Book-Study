package study.book.gyhdoca.application.port.out

import study.book.gyhdoca.domain.Account
import study.book.gyhdoca.domain.AccountId
import java.time.LocalDateTime

interface LoadAccountPort {
    fun loadAccount(accountId: AccountId, baselineDate: LocalDateTime): Account
}
