package book.realworldsoftwaredevelopment.chapter2;

import book.realworldsoftwaredevelopment.chapter2.v2.BankTransaction;
import book.realworldsoftwaredevelopment.chapter2.v4.BankStatementCSVParserV4;
import book.realworldsoftwaredevelopment.chapter2.v4.BankStatementParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

public class BankStatementCSVParserTest {

    private final BankStatementParser statementParser = new BankStatementCSVParserV4();

    // 구현할 테스트는 Assertions.fail() 로 알려주기.
//    @Test
//    public void shouldParseOneCorrectLine() throws Exception {
//        Assertions.fail("Not yet implemented");
//    }

    @Test
    public void shouldParseOneCorrectLine() throws Exception {
        final String line = "30-01-2017,-50,Tesco";

        final BankTransaction result = statementParser.parseFrom(line);

        final BankTransaction expected = new BankTransaction(LocalDate.of(2017, Month.JANUARY, 30), -50, "Tesco");
        final double tolerance = 0.0d;

        Assertions.assertEquals(expected.getDate(), result.getDate());
        Assertions.assertEquals(expected.getAmount(), result.getAmount(), tolerance);
        Assertions.assertEquals(expected.getDescription(), result.getDescription());

    }

}
