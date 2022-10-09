package study.modern_java_in_action.part1.chapter2;

import org.junit.jupiter.api.Test;
import study.modern_java_in_action.model.Apple;
import study.modern_java_in_action.model.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;

import static study.modern_java_in_action.model.Color.*;

/**
 * Chapter 2. 동작 파라미터화 코드 전달하기
 */

public class Chapter2 {

    /* 동작 파라미터화(behavior parameterization)
        - 변화하는 요구사항은 소프트웨어 엔지니어링에서 피할 수 없는 문제다. 이를 잘 대응하기 위해선,
            - 엔지니어링적인 비용이 가장 최소화될 수 있으면서,
            - 새롭게 추가한 기능은 쉽게 구현할 수 있어야 하며,
            - 장기적인 관점에서 유지보수가 쉬워야 한다.
        - 동작 파라미터화란 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블록을 의미한다.
            - 이 코드 블록은 나중에 프로그램에서 호출한다. 즉, 코드 블록의 실행은 나중으로 미뤄진다.
            - 예를 들면,
                - 리스트의 모든 요소에 대해서 '어떤 동작'을 수행할 수 있음
                - 리스트 관련 작업을 끝낸 다음에 '어떤 다른 동작'을 수행할 수 있음
                - 에러가 발생하면 '정해진 어떤 다른 동작'을 수행할 수 있음
     */


    /**
     * 2.1 변화하는 요구사항에 대응하기
     */

    /* 2.1.1 첫 번째 시도 : 녹색 사과 필터링
        - 거의 비슷한 코드가 반복 존재한다면 그 코드를 추상화한다.
     */
    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (GREEN.equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    /* 2.1.2 두 번째 시도 : 색을 파라미터화

     */
    public static List<Apple> filterApplesByColor(List<Apple> inventory, Color color) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor().equals(color)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getWeight() > weight) {
                result.add(apple);
            }
        }
        return result;
    }

    /* 2.1.3 세 번째 시도 : 가능한 모든 속성으로 필터링
        - 어떤 것을 기준으로 필터링할지 가리키는 플래그(`flag`) 추가하는 방법은 좋지 않다.
            - true / flase 의 의미가 불명확하다.
            - 앞으로 요구사항이 바뀌었을 때 유연하게 대응할 수도 없다.
     */
    // 권장하지 않음 - 특히 색이나 무게 중 어떤 것을 기준으로 필터링할지 가리키는 플래그(`flag`) 추가
    public static List<Apple> filterApples(List<Apple> inventory, Color color, int weight, boolean flag) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ((flag && apple.getColor().equals(color)) || (!flag && apple.getWeight() > weight)) {
                result.add(apple);
            }
        }
        return result;
    }


    /**
     * 2.2 동작 파라미터화
     */

    /* 프레디케이드 (Predicate)
        - 참 또는 거짓을 반환하는 함수
        - 선택 조건을 결정하는 인터페이스
     */

    public interface ApplePredicate {
        boolean test(Apple apple);
    }

    /* 전략 디자인 패턴
        - 각 알고리즘(전략이라 불리는)을 캡슐화하는 알고리즘 패밀리를 정의
        - 런타임에 알고리즘을 선택
        - 예시
            - 알고리즘 패밀리 = `ApplePredicate`
            - 전략 = `AppleHeavyWeightPredicate`, `AppleGreenColorPredicate`
     */
    public class AppleHeavyWeightPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return apple.getWeight() > 150;
        }
    }

    public class AppleGreenColorPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return GREEN.equals(apple.getColor());
        }
    }

    /* 동작 파라미터화
        - 즉 메서드가 다양한 동작(또는 전략)을 받아서 내부적으로 다양한 동작을 수행할 수 있음.
        - 기본 로직과 구체적 동작을 분리할 수 있음.
        - 한 메서드가 다른 동작을 수행하도록 재활용할 수 있다.
        - 예시
            - `filterApples` 메서드가 `ApplePredicate` 객체를 인수로 받도록 수정
     */

    /* 2.2.1 네 번째 시도 : 추상적 조건으로 필터링

     */
    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    // 코드 전달 예시
    public class AppleRedAndHeavyPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return RED.equals(apple.getColor()) && apple.getWeight() > 150;
        }
    }

    @Test
    void codeTransferExample() {
        List<Apple> inventory = Apple.exampleAppleList();
        List<Apple> redAndHeavyApples = filterApples(inventory, new AppleRedAndHeavyPredicate());
    }

    /* 퀴즈 2-1 : 유연한 prettyPrintApple 메서드 구현하기

     */
    public interface AppleFormatter {
        String accept(Apple apple);
    }


    public class AppleFancyFormatter implements AppleFormatter {
        @Override
        public String accept(Apple apple) {
            String characteristic = apple.getWeight() > 150 ? "heavy" : "light";
            return "A " + characteristic + " " + apple.getColor() + " apple";
        }
    }

    public class AppleSimpleFormatter implements AppleFormatter {
        @Override
        public String accept(Apple apple) {
            return "An apple of " + apple.getWeight() + "g";
        }
    }

    public static void prettyPrintApple(List<Apple> inventory, AppleFormatter formatter) {
        for (Apple apple : inventory) {
            String output = formatter.accept(apple);
            System.out.println(output);
        }
    }

    @Test
    void appleFormatterExample() {
        List<Apple> inventory = Apple.exampleAppleList();
        prettyPrintApple(inventory, new AppleFancyFormatter());
        prettyPrintApple(inventory, new AppleSimpleFormatter());
    }


    /**
     * 2.3 복잡한 과정 간소화
     */

    /*
        - 지금까지 예제를 보면, 새로운 동작을 전달하려면 특정 인터페이스를 구현하는 여러 클래스를 정의한 다음에 인스턴스화해야 한다.
            - 상당히 번거롭고 시간 낭비
        - 이를 해결하기 위한 방법
            - 익명 클래스
            - 람다 표현식
     */

    /* 2.3.1 익명 클래스
        - 자바의 지역 클래스(local class, 블록 내부에 선언된 클래스)와 비슷한 개념이다.
            - 이름이 없는 클래스다.
            - 익명 클래스를 이용하면 클래스 선언과 인스턴스화를 동시에 할 수 있다.
            - 즉석에서 필요한 구현을 만들어서 사용할 수 있다.
     */

    /* 2.3.2 다섯 번째 시도 : 익명 클래스 사용
        - 단점
            - 익명 클래스는 여전히 많은 공간을 차지한다.
            - 많은 프로그래머가 익명 클래스의 사용에 익숙하지 않다.
        - 코드의 장황함은 나쁜 특성이다.
            - 장황한 코드는 구현하고 유지보수하는 데 시간이 오래 걸리 뿐 아니라 읽는 즐거움을 빼앗는다. -> 개발자로부터 외면받는다.
            - 한눈에 이해할 수 있어야 좋은 코드다.
            - 익명 클래스로 인터페이스를 구현하는 여러 클래스를 선언하는 과정을 조금 줄일 수 있지만 여전히 만족스럽지 않다.
            - 코드 조각을 전달하는 과정에서 결국은 객체를 만들고 명시적으로 새로운 동작을 정의하는 메서드를 구현해야 한다는 점은 변하지 않는다.
     */
    @Test
    void usingAnonymousClassExample() {
        List<Apple> inventory = Apple.exampleAppleList();

        List<Apple> redApples = filterApples(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return RED.equals(apple.getColor());
            }
        });

        Apple.printApples(redApples);
    }

    /* 2.3.3 여섯 번째 시도 : 람다 표현식 사용
        - 코드가 훨씬 간결해진다.
            -요구사항 변화에 더 유연하게 대응할 수 있는 동작 파라미터화를 더 쉽게 사용할 수 있다.
     */
    @Test
    void usingLambdaExample() {
        List<Apple> inventory = Apple.exampleAppleList();
        List<Apple> result = filterApples(inventory, (Apple apple) -> RED.equals(apple.getColor()));
        Apple.printApples(result);
    }

    /* 2.3.4 일곱 번째 시도 : 리스트 형식으로 추상화

     */
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T e : list) {
            if (predicate.test(e)) {
                result.add(e);
            }
        }
        return result;
    }

    @Test
    void filterExample() {
        List<Apple> inventory = Apple.exampleAppleList();
        List<Apple> redApples = filter(inventory, (Apple apple) -> RED.equals(apple.getColor()));

        List<Integer> numbers = List.of(1, 2, 3, 4);
        List<Integer> evenNumbers = filter(numbers, (Integer i) -> i % 2 == 0);
    }


    /**
     * 2.4 실전 예제
     */

    /* 정리
        - 동작 파라미터화는 변화하는 요구사항에 쉽게 적응하는 유용한 패턴이다.
            - 동작 파라미터화 패턴은 동작을 한 조각의 코드로 캡슐화한 다음에 메서드로 전달해서 메서드의 동작을 파라미터화한다.
            - 이들 메서드를 익명 클래스와 자주 사용하기도 한다.
     */

    /* 2.4.1 `Comparator`로 정렬하기

     */
    @Test
    void comparatorExample() {
        List<Apple> inventory = Apple.exampleAppleList();

        // 익명 클래스 사용
        inventory.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple a1, Apple a2) {
                return a1.getWeight().compareTo(a2.getWeight());
            }
        });

        // 람다 사용
        inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
    }

    /* 2.4.2 `Runnable`로 코드 블록 실행하기

     */
    @Test
    void runnableExample() {
        // 익명 클래스 사용
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World");
            }
        });

        // 람다 사용
        Thread t2 = new Thread(() -> System.out.println("Hello World"));
    }

    /* 2.4.3 `Callable`을 결과로 반환하기

     */
    @Test
    void callableExample() {
        ExecutorService executorService = Executors.newCachedThreadPool();

        // 익명 클래스 사용
        Future<String> threadName1 = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Thread.currentThread().getName();
            }
        });

        // 람다 사용
        Future<String> threadName2 = executorService.submit(
                () -> Thread.currentThread().getName()
        );
    }

    /* 2.4.4 GUI 이벤트 처리하기

     */
//    @Test
//    void guiEventExample() {
//        Button button = new Button("send");
//
//        // 익명 클래스 사용
//        button.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent event) {
//                label.setText("Sent!!");
//            }
//        });
//
//        // 람다 사용
//        button.setOnAction((ActionEvent event) -> label.setText("Sent!!"));
//    }
}
