package study.modern_java_in_action.part3.chapter9;

public class Validator {
    private final ValidationStrategy strategy;

    public Validator(ValidationStrategy v) {
        this.strategy = v;
    }

    public boolean validate(String s) {
        return strategy.execute(s);
    }
}
