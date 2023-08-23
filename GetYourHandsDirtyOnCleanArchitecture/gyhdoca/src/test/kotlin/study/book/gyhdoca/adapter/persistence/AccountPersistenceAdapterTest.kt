package study.book.gyhdoca.adapter.persistence

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import study.book.gyhdoca.domain.Account
import study.book.gyhdoca.domain.AccountId
import study.book.gyhdoca.domain.ActivityWindow
import study.book.gyhdoca.domain.Money
import study.book.gyhdoca.testAccount
import study.book.gyhdoca.testActivity
import java.time.LocalDateTime

@DataJpaTest
@Import(AccountPersistenceAdapter::class, AccountMapper::class)
internal class AccountPersistenceAdapterTest(
    @Autowired
    private val adapter: AccountPersistenceAdapter,
    @Autowired
    private val activityRepository: ActivityRepository
) {
    @Test
    @Sql("AccountPersistenceAdapterTest.sql")
    fun loadsAccount() {
        val account: Account = adapter.loadAccount(
            accountId = AccountId(1L),
            baselineDate = LocalDateTime.of(2018, 8, 10, 0 ,0)
        )

        assertThat(account.activityWindow.activities).hasSize(2)
        assertThat(account.calculateBalance()).isEqualTo(Money.of(500L))
    }

    @Test
    fun updateActivities() {
        val account: Account = testAccount.copy(
            baselineBalance = Money.of(555L),
            activityWindow = ActivityWindow(
                testActivity.copy(id = null, money = Money.of(1L))
            )
        )
        adapter.updateActivities(account)

        assertThat(activityRepository.count()).isEqualTo(1)

        val savedActivity: ActivityJpaEntity = activityRepository.findAll().first()
        assertThat(savedActivity.amount).isEqualTo(1L)
    }
}
