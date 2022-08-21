package book.realworldsoftwaredevelopment.chapter3.v5;

import book.realworldsoftwaredevelopment.chapter2.v2.BankTransaction;

@FunctionalInterface
public interface BankTransactionFilter {
    boolean test(BankTransaction bankTransaction);
    // 이러한 인터페이스는 자바에서 이미 제공하는 Predicate<T> 를 사용하는 것이 훨씬 좋다.
}
