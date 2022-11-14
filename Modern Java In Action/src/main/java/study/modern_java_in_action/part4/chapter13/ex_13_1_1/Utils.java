package study.modern_java_in_action.part4.chapter13.ex_13_1_1;

import java.util.List;

public class Utils {
    public static void paint(List<Resizable> l) {
        l.forEach(r -> {
            r.setAbsoluteSize(42, 42);
            r.draw();
        });
    }
}
