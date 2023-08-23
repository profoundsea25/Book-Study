package study.book.gyhdoca.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import study.book.gyhdoca.testAccount
import study.book.gyhdoca.testActivity

internal class AccountTest {

    @Test
    fun calculatesBalance() {
        val accountId: AccountId = AccountId(1L)
        val account: Account = testAccount.copy(
            id = accountId,
            baselineBalance = Money.of(555L),
            activityWindow = ActivityWindow(
                testActivity.copy(targetAccountId = accountId, money = Money.of(999L)),
                testActivity.copy(targetAccountId = accountId, money = Money.of(1L))
            )
        )
        val balance: Money = account.calculateBalance()
        assertThat(balance).isEqualTo(Money.of(1555L))
    }

    @Test
    fun withdrawalSucceeds() {
        val accountId: AccountId = AccountId(1L)
        val account: Account = testAccount.copy(
            id = accountId,
            baselineBalance = Money.of(555L),
            activityWindow = ActivityWindow(
                testActivity.copy(targetAccountId = accountId, money = Money.of(999L)),
                testActivity.copy(targetAccountId = accountId, money = Money.of(1L))
            )
        )
        val success: Boolean = account.withdraw(money = Money.of(555L), targetAccountId = AccountId(99L))

        assertThat(success).isTrue
        assertThat(account.activityWindow.activities).hasSize(3)
        assertThat(account.calculateBalance()).isEqualTo(Money.of(1000L))
    }

    @Test
    fun withdrawalFailure() {
        val accountId: AccountId = AccountId(1L)
        val account: Account = testAccount.copy(
            id = accountId,
            baselineBalance = Money.of(555L),
            activityWindow = ActivityWindow(
                testActivity.copy(targetAccountId = accountId, money = Money.of(999L)),
                testActivity.copy(targetAccountId = accountId, money = Money.of(1L))
            )
        )
        val success: Boolean = account.withdraw(money = Money.of(1556L), targetAccountId = AccountId(99L))

        assertThat(success).isFalse
        assertThat(account.activityWindow.activities).hasSize(2)
        assertThat(account.calculateBalance()).isEqualTo(Money.of(1555L))
    }

    @Test
    fun depositSuccess() {
        val accountId: AccountId = AccountId(1L)
        val account: Account = testAccount.copy(
            id = accountId,
            baselineBalance = Money.of(555L),
            activityWindow = ActivityWindow(
                testActivity.copy(targetAccountId = accountId, money = Money.of(999L)),
                testActivity.copy(targetAccountId = accountId, money = Money.of(1L))
            )
        )
        val success: Boolean = account.deposit(money = Money.of(445L), sourceAccountId = AccountId(99L))

        assertThat(success).isTrue
        assertThat(account.activityWindow.activities).hasSize(3)
        assertThat(account.calculateBalance()).isEqualTo(Money.of(2000L))
    }
}
