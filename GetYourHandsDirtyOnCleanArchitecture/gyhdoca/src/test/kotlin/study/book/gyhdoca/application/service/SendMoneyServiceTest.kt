package study.book.gyhdoca.application.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.*
import org.mockito.Mockito
import study.book.gyhdoca.any
import study.book.gyhdoca.application.port.`in`.SendMoneyCommand
import study.book.gyhdoca.application.port.out.AccountLock
import study.book.gyhdoca.application.port.out.LoadAccountPort
import study.book.gyhdoca.application.port.out.UpdateAccountStatePort
import study.book.gyhdoca.capture
import study.book.gyhdoca.domain.Account
import study.book.gyhdoca.domain.AccountId
import study.book.gyhdoca.domain.Money
import study.book.gyhdoca.eq
import java.time.LocalDateTime

internal class SendMoneyServiceTest {
    private val loadAccountPort: LoadAccountPort = Mockito.mock(LoadAccountPort::class.java)
    private val accountLock: AccountLock = Mockito.mock(AccountLock::class.java)
    private val updateAccountStatePort: UpdateAccountStatePort = Mockito.mock(UpdateAccountStatePort::class.java)
    private val sendMoneyService: SendMoneyService = SendMoneyService(loadAccountPort, accountLock, updateAccountStatePort)

    @Test
    fun transactionSucceeds() {
        val sourceAccount: Account = givenSourceAccount()
        val targetAccount: Account = givenTargetAccount()

        givenWithdrawalWillSucceed(sourceAccount)
        givenDepositWillSucceed(targetAccount)

        val money: Money = Money.of(500L)

        val command: SendMoneyCommand = SendMoneyCommand(
            sourceAccountId = sourceAccount.id!!,
            targetAccountId = targetAccount.id!!,
            money = money
        )

        val success: Boolean = sendMoneyService.sendMoney(command = command)

        assertThat(success).isTrue

        val sourceAccountId: AccountId = sourceAccount.id!!
        val targetAccountId: AccountId = targetAccount.id!!

        then(accountLock).should().lockAccount(eq(sourceAccountId))
        then(sourceAccount).should().withdraw(eq(money), eq(targetAccountId))
        then(accountLock).should().releaseAccount(eq(sourceAccountId))

        then(accountLock).should().lockAccount(eq(targetAccountId))
        then(targetAccount).should().deposit(eq(money), eq(sourceAccountId))
        then(accountLock).should().releaseAccount(eq(targetAccountId))

        thenAccountsHaveBeenUpdated(sourceAccountId, targetAccountId)
    }

    private fun givenAnAccountWithId(id: AccountId): Account {
        val account: Account = Mockito.mock(Account::class.java)
        given(account.id).willReturn(id)
        given(loadAccountPort.loadAccount(eq(account.id!!), any(LocalDateTime::class.java))).willReturn(account)
        return account
    }

    private fun givenSourceAccount(): Account {
        return givenAnAccountWithId(AccountId(41L))
    }

    private fun givenTargetAccount(): Account {
        return givenAnAccountWithId(AccountId(42L))
    }

    private fun givenWithdrawalWillSucceed(account: Account) {
        given(account.withdraw(any(Money::class.java), any(AccountId::class.java))).willReturn(true)
    }

    private fun givenDepositWillSucceed(account: Account) {
        given(account.deposit(any(Money::class.java), any(AccountId::class.java))).willReturn(true)
    }

    private fun thenAccountsHaveBeenUpdated(vararg accountIds: AccountId) {
        val accountCaptor: ArgumentCaptor<Account> = ArgumentCaptor.forClass(Account::class.java)
        then(updateAccountStatePort).should(times(accountIds.size)).updateActivities(capture(accountCaptor))

        val updatedAccountIds: List<AccountId> = accountCaptor.allValues
            .mapNotNull { it.id }

        assertThat(updatedAccountIds).containsExactlyInAnyOrderElementsOf(accountIds.toMutableList())
    }
}
