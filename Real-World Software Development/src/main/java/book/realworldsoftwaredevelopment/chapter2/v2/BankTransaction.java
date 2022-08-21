package book.realworldsoftwaredevelopment.chapter2.v2;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BankTransaction {

    private final LocalDate date;
    private final double amount;
    private final String description;

}
