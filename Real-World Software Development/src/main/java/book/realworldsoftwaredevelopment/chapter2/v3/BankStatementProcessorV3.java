package book.realworldsoftwaredevelopment.chapter2.v3;

import book.realworldsoftwaredevelopment.chapter2.v2.BankTransaction;
import lombok.RequiredArgsConstructor;

import java.time.Month;
import java.util.List;

@RequiredArgsConstructor
public class BankStatementProcessorV3 {

    private final List<BankTransaction> bankTransactions;

    public double calculateTotalAmount() {
        double total = 0;
        for (BankTransaction bankTransaction : bankTransactions) {
            total += bankTransaction.getAmount();
        }
        return total;
    }

    public double caculateTotalInMonth(final Month month) {
        double total = 0;
        for (BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month) {
                total += bankTransaction.getAmount();
            }
        }
        return total;
    }

    public double calculateTotalForCategory(final String category) {
        double total = 0;
        for (BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDescription().equals(category)) {
                total += bankTransaction.getAmount();
            }
        }
        return total;
    }
}
