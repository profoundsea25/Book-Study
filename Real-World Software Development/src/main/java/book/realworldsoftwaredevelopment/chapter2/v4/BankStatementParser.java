package book.realworldsoftwaredevelopment.chapter2.v4;

import book.realworldsoftwaredevelopment.chapter2.v2.BankTransaction;

import java.util.List;

public interface BankStatementParser {

    BankTransaction parseFrom(String line);

    List<BankTransaction> parseLinesFrom(List<String> lines);
}
