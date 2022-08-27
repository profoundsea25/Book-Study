package book.realworldsoftwaredevelopment.chapter3.v6;

import book.realworldsoftwaredevelopment.chapter2.v2.BankTransaction;

@FunctionalInterface
public interface BankTransactionSummarizer {
    double summarize(double accumulator, BankTransaction bankTransaction);
}
