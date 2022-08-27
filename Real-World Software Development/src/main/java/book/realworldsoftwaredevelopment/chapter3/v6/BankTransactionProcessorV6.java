package book.realworldsoftwaredevelopment.chapter3.v6;

import book.realworldsoftwaredevelopment.chapter2.v2.BankTransaction;
import book.realworldsoftwaredevelopment.chapter3.v5.BankTransactionFilter;
import lombok.RequiredArgsConstructor;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BankTransactionProcessorV6 {

    private final List<BankTransaction> bankTransactions;

    public double summarizeTransactions(final BankTransactionSummarizer bankTransactionSummarizer) {
        double result = 0;
        for (BankTransaction bankTransaction : bankTransactions) {
            result = bankTransactionSummarizer.summarize(result, bankTransaction);
        }
        return result;
    }

    public double calculateTotalInMonth(final Month month) {
        return summarizeTransactions((acc, bankTransaction)
                -> bankTransaction.getDate().getMonth() == month ? acc + bankTransaction.getAmount() : acc);
    }

    // ...

    public List<BankTransaction> findTransactions(final BankTransactionFilter bankTransactionFilter) {
        final List<BankTransaction> result = new ArrayList<>();
        for (BankTransaction bankTransaction : bankTransactions) {
            if (bankTransactionFilter.test(bankTransaction)) {
                result.add(bankTransaction);
            }
        }
        return result;
    }

    public List<BankTransaction> findTransactionGreaterThanEqual(final int amount) {
        return findTransactions(bankTransaction -> bankTransaction.getAmount() >= amount);
    }

    // 스트림 활용 예시
    public List<BankTransaction> findTransactionGreaterThanEqualByStream(final int amount) {
        return bankTransactions.stream()
                .filter(bankTransaction -> bankTransaction.getAmount() >= amount)
                .collect(Collectors.toList());
    }

    // ...
}
