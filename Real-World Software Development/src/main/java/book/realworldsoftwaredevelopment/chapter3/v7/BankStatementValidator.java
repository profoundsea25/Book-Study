package book.realworldsoftwaredevelopment.chapter3.v7;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class BankStatementValidator {

    private String description;
    private String date;
    private String amount;

    public BankStatementValidator(final String description, final String date, final String amount) {
        this.description = Objects.requireNonNull(description);
        this.date = Objects.requireNonNull(date);
        this.amount = Objects.requireNonNull(amount);
    }

    public boolean overSpecificValidate() throws DescriptionTooLongException, InvalidDateFormat, DateInFutureException, InvalidAmountException{

        if (this.description.length() > 100) {
            throw new DescriptionTooLongException();
        }

        final LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(this.date);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormat();
        }

        if (parsedDate.isAfter(LocalDate.now())) {
            throw new DateInFutureException();
        }

        try {
            Double.parseDouble(this.amount);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException();
        }
        return true;

    }

    private static class DescriptionTooLongException extends Exception {
    }

    private static class InvalidDateFormat extends Exception {
    }

    private static class DateInFutureException extends Exception {
    }

    private static class InvalidAmountException extends Exception {
    }

    public boolean overGeneralValidate() {

        if (this.description.length() > 100) {
            throw new IllegalArgumentException("The description is too long");
        }

        final LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(this.date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid format for date", e);
        }

        if (parsedDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("date cannot be in the future");
        }

        try {
            Double.parseDouble(this.amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid format for amount", e);
        }
        return true;
    }

    public Notification validate() {
        final Notification notification = new Notification();

        if (this.description.length() > 100) {
            notification.addError("The description is too long");
        }

        final LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(this.date);
            if (parsedDate.isAfter(LocalDate.now())) {
                notification.addError("date cannot be in the future");
            }
        } catch (DateTimeParseException e) {
            notification.addError("Invalid format for date");
        }

        final double amount;
        try {
            amount = Double.parseDouble(this.amount);
        } catch (NumberFormatException e) {
            notification.addError("Invalid format for amount");
        }
        return notification;
    }

}
