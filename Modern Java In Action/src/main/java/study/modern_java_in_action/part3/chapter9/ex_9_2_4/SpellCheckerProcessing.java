package study.modern_java_in_action.part3.chapter9.ex_9_2_4;

public class SpellCheckerProcessing extends ProcessingObject<String> {

    @Override
    protected String handleWork(String text) {
        return text.replaceAll("labda", "lambda");
    }

}
