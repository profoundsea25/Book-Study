package book.realworldsoftwaredevelopment.chapter2.v4;

import java.io.IOException;

public class MainApplication {

    public static void main(String[] args) throws IOException {

        final BankStatementAnalyzerV4 bankStatementAnalyzer = new BankStatementAnalyzerV4();

        final BankStatementParser bankStatementParser = new BankStatementCSVParserV4();

        bankStatementAnalyzer.analyze(args[0], bankStatementParser);
    }
}
