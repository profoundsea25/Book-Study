package study.book.gyhdoca.adapter.`in`.web

import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import study.book.gyhdoca.application.port.`in`.SendMoneyCommand
import study.book.gyhdoca.application.port.`in`.SendMoneyUseCase
import study.book.gyhdoca.domain.AccountId
import study.book.gyhdoca.domain.Money
import study.book.gyhdoca.eq

@WebMvcTest(controllers = [SendMoneyController::class])
internal class SendMoneyControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
) {
    @MockBean
    lateinit var sendMoneyUseCase: SendMoneyUseCase

    @Test
    fun testSendMoney() {
        mockMvc
            .perform(
                post("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}", 41L, 42L, 500)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk)

        then(sendMoneyUseCase)
            .should()
            .sendMoney(
                eq(SendMoneyCommand(
                    sourceAccountId = AccountId(41L),
                    targetAccountId = AccountId(42L),
                    money = Money.of(500L))
                )
            )
    }
}
