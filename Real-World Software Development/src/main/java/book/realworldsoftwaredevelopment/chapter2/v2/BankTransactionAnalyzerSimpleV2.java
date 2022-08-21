package book.realworldsoftwaredevelopment.chapter2.v2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class BankTransactionAnalyzerSimpleV2 {

    private static final String RESOURCES = "src/main/resources/";

    public static void main(final String... args) throws IOException {

        final BankStatementCSVParserV2 bankStatementCSVParser = new BankStatementCSVParserV2();

        // 파일 읽기
        final String fileName = args[0];
        final Path path = Paths.get(RESOURCES + fileName);
        final List<String> lines = Files.readAllLines(path);

        final List<BankTransaction> bankTransactions = bankStatementCSVParser.parseLinesFromCSV(lines);

        System.out.println("The total for all transactions is " + calculateTotalAmount(bankTransactions));
        System.out.println("Transactions in January" + selectInMonth(bankTransactions, Month.JANUARY));
    }

    private static double calculateTotalAmount(final List<BankTransaction> bankTransactions) {
        double total = 0d;
        for (BankTransaction bankTransaction : bankTransactions) {
            total += bankTransaction.getAmount();
        }
        return total;
    }

    private static List<BankTransaction> selectInMonth(final List<BankTransaction> bankTransactions, final Month month) {
        final List<BankTransaction> bankTransactionsInMonth = new ArrayList<>();
        for (BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month)
            bankTransactionsInMonth.add(bankTransaction);
        }
        return bankTransactionsInMonth;
    }

}
