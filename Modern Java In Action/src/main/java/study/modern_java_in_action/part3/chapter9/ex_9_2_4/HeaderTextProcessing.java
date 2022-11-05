package study.modern_java_in_action.part3.chapter9.ex_9_2_4;

public class HeaderTextProcessing extends ProcessingObject<String> {

    public String handleWork(String text) {
        return "From Raoul, Mario and Alan: " + text;
    }

}
