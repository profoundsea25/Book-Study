package study.book.gyhdoca.adapter.web

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import study.book.gyhdoca.application.port.`in`.SendMoneyCommand
import study.book.gyhdoca.application.port.`in`.SendMoneyUseCase
import study.book.gyhdoca.domain.Money

/**
 * 안티패턴 : 하나의 컨트롤러 클래스에 기능을 합치기
 */
@RestController
class AccountController(
    private val sendMoneyUseCase: SendMoneyUseCase,
) {
    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    fun sendMoney(
        @PathVariable("sourceAccountId") sourceAccountId: Long,
        @PathVariable("targetAccountId") targetAccountId: Long,
        @PathVariable("amount") amount: Long,
    ) {
        val command: SendMoneyCommand = SendMoneyCommand(
            AccountId(sourceAccountId),
            AccountId(targetAccountId),
            Money.of(amount)
        )
        sendMoneyUseCase.sendMoney(command)
    }
}