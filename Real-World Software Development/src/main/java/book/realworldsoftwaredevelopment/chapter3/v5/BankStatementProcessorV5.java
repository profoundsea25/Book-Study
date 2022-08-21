package book.realworldsoftwaredevelopment.chapter3.v5;

import book.realworldsoftwaredevelopment.chapter2.v2.BankTransaction;
import lombok.RequiredArgsConstructor;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BankStatementProcessorV5 {

    private final List<BankTransaction> bankTransactions;

    // 비추천 - findTransaction~으로 시작하는 filter 기능의 함수를 아래와 같이 구현하면 코드가 반복된다.
    public List<BankTransaction> findTransactionGreaterThanEqual(final int amount) {
        final List<BankTransaction> result = new ArrayList<>();
        for (BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getAmount() >= amount) {
                result.add(bankTransaction);
            }
        }
        return result;
    }

    public List<BankTransaction> findTransactionInMonth(final Month month) {
        final List<BankTransaction> result = new ArrayList<>();
        for (BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month) {
                result.add(bankTransaction);
            }
        }
        return result;
    }

    public List<BankTransaction> findTransactionInMonthAndGreater(final Month month, final int amount) {
        final List<BankTransaction> result = new ArrayList<>();
        for (BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month && bankTransaction.getAmount() >= amount) {
                result.add(bankTransaction);
            }
        }
        return result;
    }

    // 추천 - findTransaction~을 함수형 인터페이스로 구현
    public List<BankTransaction> findTransactions(final BankTransactionFilter bankTransactionFilter) {
        final List<BankTransaction> result = new ArrayList<>();
        for (BankTransaction bankTransaction : bankTransactions) {
            if (bankTransactionFilter.test(bankTransaction)) {
                result.add(bankTransaction);
            }
        }
        return result;
    }
}
