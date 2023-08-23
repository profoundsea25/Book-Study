package study.book.gyhdoca.application.port.out

import study.book.gyhdoca.domain.AccountId

interface AccountLock {
    fun lockAccount(accountId: AccountId)
    fun releaseAccount(accountId: AccountId)
}
