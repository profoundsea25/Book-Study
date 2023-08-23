package study.book.gyhdoca.adapter.`in`.web

import org.springframework.web.bind.annotation.*
import study.book.gyhdoca.application.port.`in`.SendMoneyCommand
import study.book.gyhdoca.application.port.`in`.SendMoneyUseCase
import study.book.gyhdoca.domain.AccountId
import study.book.gyhdoca.domain.Money

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
        val command: SendMoneyCommand = SendMoneyCommand(
            sourceAccountId = AccountId(sourceAccountId),
            targetAccountId = AccountId(targetAccountId),
            money = Money.of(amount)
        )
        sendMoneyUseCase.sendMoney(command = command)
    }
}
