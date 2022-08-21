package book.realworldsoftwaredevelopment.chapter3.v5;

import book.realworldsoftwaredevelopment.chapter2.v2.BankTransaction;
import book.realworldsoftwaredevelopment.chapter2.v4.BankStatementParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.List;

public class BankStatementAnalyzerV5 {

    private static final String RESOURCES = "src/main/resources/";

    public void analyze(final String fileName, final BankStatementParser bankStatementParser) throws IOException {
        // 파일 읽기
        final Path path = Paths.get(RESOURCES + fileName);
        final List<String> lines = Files.readAllLines(path);

        final List<BankTransaction> bankTransactions = bankStatementParser.parseLinesFrom(lines);
        final BankStatementProcessorV5 bankStatementProcessor = new BankStatementProcessorV5(bankTransactions);

        // 비추천 - 함수형 인터페이스를 활용한 특정 조건을 검색
        final List<BankTransaction> transactionsByNonLambda
                = bankStatementProcessor.findTransactions(new BankTransactionIsInFebruaryAndExpensive());

        // 추천 - 람다 활용
        final List<BankTransaction> transactionsByLambda = bankStatementProcessor.findTransactions(
                bankTransaction ->
                        bankTransaction.getDate().getMonth() == Month.FEBRUARY &&
                        bankTransaction.getAmount() >= 1_000);

    }

}
