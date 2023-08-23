package study.book.gyhdoca

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class SendMoneySystemTest(
    @Autowired
    private val restTemplate: TestRestTemplate
) {
    @Test
    @Sql("SendMoneySystemTest.sql")
    fun sendMoney() {
    }
}
