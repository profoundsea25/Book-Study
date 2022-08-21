package book.realworldsoftwaredevelopment.chapter2.v3;

import book.realworldsoftwaredevelopment.chapter2.v2.BankStatementCSVParserV2;
import book.realworldsoftwaredevelopment.chapter2.v2.BankTransaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.List;

public class BankStatementAnalyzerV3 {

    private static final String RESOURCES = "src/main/resources/";
    private static final BankStatementCSVParserV2 bankStatementParser = new BankStatementCSVParserV2();

    public static void main(final String... args) throws IOException {
        // 파일 읽기
        final String fileName = args[0];
        final Path path = Paths.get(RESOURCES + fileName);
        final List<String> lines = Files.readAllLines(path);

        final List<BankTransaction> bankTransactions = bankStatementParser.parseLinesFromCSV(lines);
        final BankStatementProcessorV3 bankStatementProcessor = new BankStatementProcessorV3(bankTransactions);

        collectSummary(bankStatementProcessor);
    }

    private static void collectSummary(BankStatementProcessorV3 bankStatementProcessor) {
        System.out.println("The total for all transactions is " + bankStatementProcessor.calculateTotalAmount());
        System.out.println("The total for all transactions in January is " + bankStatementProcessor.caculateTotalInMonth(Month.JANUARY));
        System.out.println("The total for all transactions in February is " + bankStatementProcessor.caculateTotalInMonth(Month.FEBRUARY));
        System.out.println("The total salary received is " + bankStatementProcessor.calculateTotalForCategory("Salary"));
    }

}
