package study.modern_java_in_action.model;

import lombok.Data;

import java.util.List;

@Data
public class Apple {

    private final Integer weight;
    private final Color color;

    public static List<Apple> exampleAppleList() {
        return List.of(
                new Apple(80, Color.GREEN),
                new Apple(155, Color.GREEN),
                new Apple(120, Color.RED)
        );
    }

    public static void printApples(List<Apple> appleList, AppleFormatter appleFormatter) {
        for (Apple apple : appleList) {
            System.out.println(appleFormatter.accept(apple));
        }
    }

    public static void printApples(List<Apple> appleList) {
        printApples(
            appleList,
            apple -> String.format(
                "Apple Weight = %d, Color = %s",
                apple.getWeight(),
                apple.getColor()
            )
        );
    }
}
