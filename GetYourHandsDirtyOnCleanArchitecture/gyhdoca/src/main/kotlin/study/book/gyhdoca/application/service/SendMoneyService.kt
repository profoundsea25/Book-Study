package study.book.gyhdoca.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import study.book.gyhdoca.application.port.`in`.SendMoneyCommand
import study.book.gyhdoca.application.port.`in`.SendMoneyUseCase
import study.book.gyhdoca.application.port.out.AccountLock
import study.book.gyhdoca.application.port.out.LoadAccountPort
import study.book.gyhdoca.application.port.out.UpdateAccountStatePort
import study.book.gyhdoca.domain.Account
import study.book.gyhdoca.domain.AccountId
import java.time.LocalDateTime

@Transactional
@Service
class SendMoneyService(
    private val loadAccountPort: LoadAccountPort,
    private val accountLock: AccountLock,
    private val updateAccountStatePort: UpdateAccountStatePort
) : SendMoneyUseCase {
    override fun sendMoney(command: SendMoneyCommand): Boolean {
        val baselineDate: LocalDateTime = LocalDateTime.now().minusDays(10L)

        val sourceAccount: Account = loadAccountPort.loadAccount(accountId = command.sourceAccountId, baselineDate = baselineDate)
        val targetAccount: Account = loadAccountPort.loadAccount(accountId = command.targetAccountId, baselineDate = baselineDate)

        val sourceAccountId: AccountId = sourceAccount.id ?: throw IllegalArgumentException("expected source account ID not to be empty")
        val targetAccountId: AccountId = targetAccount.id ?: throw IllegalArgumentException("expected target account ID not to be empty")

        accountLock.lockAccount(sourceAccountId)
        if (!sourceAccount.withdraw(money = command.money, targetAccountId = targetAccountId)) {
            accountLock.releaseAccount(sourceAccountId)
            return false
        }

        accountLock.lockAccount(targetAccountId)
        if (!targetAccount.deposit(money = command.money, sourceAccountId = sourceAccountId)) {
            accountLock.releaseAccount(sourceAccountId)
            accountLock.releaseAccount(targetAccountId)
        }

        updateAccountStatePort.updateActivities(sourceAccount)
        updateAccountStatePort.updateActivities(targetAccount)

        accountLock.releaseAccount(sourceAccountId)
        accountLock.releaseAccount(targetAccountId)
        return true
    }
}
