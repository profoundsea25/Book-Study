package study.modern_java_in_action.part3.chapter9.ex_9_2_1;

public class IsAllLowerCase implements ValidationStrategy {
    public boolean execute(String s) {
        return s.matches("[a-z]+");
    }
}
