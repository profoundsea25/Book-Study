package study.book.gyhdoca.application.service

import org.springframework.stereotype.Component
import study.book.gyhdoca.application.port.out.AccountLock
import study.book.gyhdoca.domain.AccountId

@Component
class NoOpAccountLock : AccountLock {
    override fun lockAccount(accountId: AccountId) {
        // do nothing
    }

    override fun releaseAccount(accountId: AccountId) {
        // do nothing
    }

}
