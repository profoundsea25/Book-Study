package study.book.gyhdoca

import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.jdbc.Sql
import study.book.gyhdoca.application.port.out.LoadAccountPort
import study.book.gyhdoca.domain.Account
import study.book.gyhdoca.domain.AccountId
import study.book.gyhdoca.domain.Money
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class SendMoneySystemTest(
    @Autowired
    private val restTemplate: TestRestTemplate,
    @Autowired
    private val loadAccountPort: LoadAccountPort
) {
    @Test
    @Sql("SendMoneySystemTest.sql")
    fun sendMoney() {
        val initialSourceBalance: Money = sourceAccount().calculateBalance()
        val initialTargetBalance: Money = targetAccount().calculateBalance()

        val response: ResponseEntity<Unit> = whenSendMoney(
            sourceAccountId = sourceAccountId,
            targetAccountId = targetAccountId,
            amount = transferredAmount
        )

        then(response.statusCode).isEqualTo(HttpStatus.OK)
        then(sourceAccount().calculateBalance()).isEqualTo(initialSourceBalance.minus(transferredAmount))
        then(targetAccount().calculateBalance()).isEqualTo(initialTargetBalance.plus(transferredAmount))
    }

    private val sourceAccountId: AccountId = AccountId(1L)
    private val targetAccountId: AccountId = AccountId(2L)

    private fun loadAccount(accountId: AccountId): Account {
        return loadAccountPort.loadAccount(
            accountId = accountId,
            baselineDate = LocalDateTime.now()
        )
    }

    private fun sourceAccount(): Account = loadAccount(sourceAccountId)
    private fun targetAccount(): Account = loadAccount(targetAccountId)
    private val transferredAmount: Money = Money.of(500L)

    private fun whenSendMoney(
        sourceAccountId: AccountId,
        targetAccountId: AccountId,
        amount: Money
    ): ResponseEntity<Unit> {
        val headers: HttpHeaders = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        val request: HttpEntity<Unit> = HttpEntity(null, headers)

        return restTemplate.exchange(
            "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
            HttpMethod.POST,
            request,
            Unit::class.java,
            sourceAccountId.value,
            targetAccountId.value,
            amount.amount
        )
    }
}
