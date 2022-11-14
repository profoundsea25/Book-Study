package study.modern_java_in_action.part4.chapter13.ex_13_1_1;

import java.util.Arrays;
import java.util.List;

public class Game {
    public static void main(String... args) {
        List<Resizable> resizableShapes =
                Arrays.asList(new Square(), new Rectangle(), new Ellipse());
        Utils.paint(resizableShapes);
    }
}
