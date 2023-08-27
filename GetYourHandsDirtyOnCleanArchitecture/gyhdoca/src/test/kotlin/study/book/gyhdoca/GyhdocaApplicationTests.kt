package study.book.gyhdoca

import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.springframework.boot.test.context.SpringBootTest
import study.book.gyhdoca.domain.*
import java.time.LocalDateTime

@SpringBootTest
class GyhdocaApplicationTests {
    @Test
    fun contextLoads() {
    }
}

val testActivity: Activity = Activity(
    ownerAccountId = AccountId(42L),
    sourceAccountId = AccountId(42L),
    targetAccountId = AccountId(41L),
    timestamp = LocalDateTime.now(),
    money = Money.of(999L)
)

val testAccount: Account = Account.withId(
    accountId = AccountId(42L),
    activityWindow = ActivityWindow(testActivity, testActivity),
    baselineBalance = Money.of(999L)
)

fun <T> any(clazz: Class<T>): T = ArgumentMatchers.any()

fun <T> eq(obj: T): T = ArgumentMatchers.eq(obj)

fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
