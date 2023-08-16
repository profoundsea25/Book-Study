package study.book.gyhdoca.adapter.`in`.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import study.book.gyhdoca.application.port.`in`.GetAccountBalanceQuery
import study.book.gyhdoca.application.port.`in`.SendMoneyUseCase

@RestController
class SendMoneyController(
    private val sendMoneyUseCase: SendMoneyUseCase,
) {
    @GetMapping("/accounts")
    fun listAccounts(): List<AccountResource> {
        TODO()
    }

    @GetMapping("/accounts/{accountId}")
    fun getAccount(@PathVariable("accountId") accountId: Long): AccountResource {
        TODO()
    }

    @GetMapping("/accounts/{accountId}/balance")
    fun getAccountBalance(@PathVariable("accountId") accountId: Long) {
        TODO()
    }

    @PostMapping("/accounts")
    fun createAccount(@RequestBody account: AccountResource): AccountResource {
        TODO()
    }

    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    fun sendMoney(
        @PathVariable("sourceAccountId") sourceAccountId: Long,
        @PathVariable("targetAccountId") targetAccountId: Long,
        @PathVariable("amount") amount: Long,
    ) {
        TODO()
    }
}