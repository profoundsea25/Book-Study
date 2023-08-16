package study.book.gyhdoca.application.service

import org.springframework.transaction.annotation.Transactional
import study.book.gyhdoca.application.port.`in`.SendMoneyCommand
import study.book.gyhdoca.application.port.`in`.SendMoneyUseCase
import study.book.gyhdoca.application.port.out.LoadAccountPort
import study.book.gyhdoca.application.port.out.UpdateAccountStatePort

@Transactional
class SendMoneyService(
    private val loadAccountPort: LoadAccountPort,
    private val accountLock: AccountLock,
    private val updateAccountStatePort: UpdateAccountStatePort
) : SendMoneyUseCase {
    override fun sendMoney(command: SendMoneyCommand): Boolean {
        TODO("비즈니스 규칙 검증")
        TODO("모델 상태 조작")
        TODO("출력 값 반환")
    }
}