package study.book.gyhdoca.adapter.persistence

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Component
import study.book.gyhdoca.application.port.out.LoadAccountPort
import study.book.gyhdoca.application.port.out.UpdateAccountStatePort
import study.book.gyhdoca.domain.Account
import study.book.gyhdoca.domain.AccountId
import java.time.LocalDateTime

@Component
class AccountPersistenceAdapter(
    private val accountRepository: AccountRepository,
    private val activityRepository: ActivityRepository,
    private val accountMapper: AccountMapper
) : LoadAccountPort, UpdateAccountStatePort {
    override fun loadAccount(accountId: AccountId, baselineDate: LocalDateTime): Account {
        val account: AccountJpaEntity = accountRepository.findById(accountId.value).orElseThrow { EntityNotFoundException() }
        val activities: List<ActivityJpaEntity> = activityRepository.findByOwnerSince(accountId.value, baselineDate)
        val withdrawalBalance: Long = activityRepository.getWithdrawalBalanceUntil(accountId.value, baselineDate) ?: 0L
        val depositBalance: Long = activityRepository.getDepositBalanceUntil(accountId.value, baselineDate) ?: 0L
        return accountMapper.mapToDomainEntity(account, activities, withdrawalBalance, depositBalance)
    }

    override fun updateActivities(account: Account) {
        account.activityWindow.activities.asSequence()
            .filter { it.id == null }
            .forEach { activityRepository.save(accountMapper.mapToJpaEntity(it)) }
    }
}
