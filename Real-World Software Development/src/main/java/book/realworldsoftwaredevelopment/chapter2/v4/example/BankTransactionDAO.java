package book.realworldsoftwaredevelopment.chapter2.v4.example;

import book.realworldsoftwaredevelopment.chapter2.v2.BankTransaction;

import java.time.LocalDate;

public class BankTransactionDAO {

    public BankTransaction create(final LocalDate date, final double amount, final String description) {
        // ...
        throw new UnsupportedOperationException();
    }

    public BankTransaction read(final long id) {
        // ...
        throw new UnsupportedOperationException();
    }

    public BankTransaction update(final long id) {
        // ...
        throw new UnsupportedOperationException();
    }

    public BankTransaction delete(final BankTransaction bankTransaction) {
        // ...
        throw new UnsupportedOperationException();
    }
}
